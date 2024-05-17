package com.ajou_nice.with_pet.admin.service;

import com.ajou_nice.with_pet.admin.model.dto.add_critical.AddCriticalServiceRequest;
import com.ajou_nice.with_pet.admin.model.dto.add_critical.AddWithPetServiceRequest;
import com.ajou_nice.with_pet.admin.model.dto.update_critical.UpdateCriticalServiceRequest;
import com.ajou_nice.with_pet.admin.model.dto.update_critical.UpdateCriticalServiceResponse;
import com.ajou_nice.with_pet.admin.model.dto.update_service.UpdateWithPetServiceRequest;
import com.ajou_nice.with_pet.critical_service.model.dto.CriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import com.ajou_nice.with_pet.critical_service.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterBasicResponse;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.petsitter.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.withpet_service.model.dto.WithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import com.ajou_nice.with_pet.withpet_service.repository.WithPetServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PetSitterRepository petSitterRepository;
    private final WithPetServiceRepository withPetServiceRepository;
    private final CriticalServiceRepository criticalServiceRepository;

//    private final NotificationService notificationService;

    public List<PetSitterBasicResponse> showPetSitters(String email) {
        adminValidation(email);
        List<PetSitter> petSitters = petSitterRepository.findAll();
        return PetSitterBasicResponse.toList(petSitters);
    }

    @Transactional
    public CriticalServiceResponse addCriticalService(String email,
                                                      AddCriticalServiceRequest addCriticalServiceRequest) {
        adminValidation(email);
        existCriticalServiceValidation(addCriticalServiceRequest);

        CriticalService criticalService = CriticalService.toEntity(addCriticalServiceRequest);
        CriticalService newCriticalService = criticalServiceRepository.save(criticalService);

        return CriticalServiceResponse.of(newCriticalService);
    }

    @Transactional
    public WithPetServiceResponse addWithPetService(String email,
                                                    AddWithPetServiceRequest addWithPetServiceRequest) {
        adminValidation(email);
        WithPetService withPetService = WithPetService.toEntity(addWithPetServiceRequest);
        WithPetService newWithPetService = withPetServiceRepository.save(withPetService);

        return WithPetServiceResponse.of(newWithPetService);
    }

    @Transactional
    public UpdateCriticalServiceResponse updateCriticalService(String email, Long serviceId, UpdateCriticalServiceRequest updateCriticalServiceRequest) {
        adminValidation(email);
        CriticalService criticalService = criticalServiceValidation(serviceId);
        criticalService.updateServiceInfo(updateCriticalServiceRequest);
        return UpdateCriticalServiceResponse.of(criticalService);
    }

    @Transactional
    public WithPetServiceResponse updateWithPetService(String email,Long serviceId, UpdateWithPetServiceRequest updateWithPetServiceRequest) {
        adminValidation(email);
        WithPetService withPetService = withPetServiceValidation(serviceId);
        withPetService.updateServiceInfo(updateWithPetServiceRequest);
        return WithPetServiceResponse.of(withPetService);
    }

    @Transactional
    public List<WithPetServiceResponse> deleteWithPetService(String email, Long serviceId) {
        adminValidation(email);
        WithPetService withPetService = withPetServiceValidation(serviceId);

        withPetServiceRepository.delete(withPetService);
        List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

        return WithPetServiceResponse.toList(withPetServiceList);
    }

    private CriticalService criticalServiceValidation(Long serviceId) {
        CriticalService criticalService = criticalServiceRepository.findById(serviceId)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.CRITICAL_SERVICE_NOT_FOUND,
                            ErrorCode.CRITICAL_SERVICE_NOT_FOUND.getMessage());
                });

        return criticalService;
    }

    private WithPetService withPetServiceValidation(Long serviceId) {
        WithPetService withPetService = withPetServiceRepository.findById(serviceId)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND,
                            ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
                });

        return withPetService;
    }

    private void adminValidation(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        if (!user.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ADMIN, ErrorCode.UNAUTHORIZED_ADMIN.getMessage());
        }
    }

    private void existCriticalServiceValidation(AddCriticalServiceRequest addCriticalServiceRequest) {
        List<CriticalService> criticalServiceList = criticalServiceRepository.findCritiCalServiceByServiceName(addCriticalServiceRequest.getServiceName());
        if(!criticalServiceList.isEmpty()){
            throw new AppException(ErrorCode.DUPlICATED_SERVICE,ErrorCode.DUPlICATED_SERVICE.getMessage());
        }
    }
}
