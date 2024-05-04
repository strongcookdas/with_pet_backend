package com.ajou_nice.with_pet.admin.util;

import com.ajou_nice.with_pet.admin.model.criticalservice.AddCriticalServiceRequest;
import com.ajou_nice.with_pet.critical_service.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.critical_service.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminValidation {
    private final UserRepository userRepository;
    private final CriticalServiceRepository criticalServiceRepository;

    public void adminValidation(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        if (!user.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ADMIN, ErrorCode.UNAUTHORIZED_ADMIN.getMessage());
        }
    }

    public void existCriticalServiceValidation(AddCriticalServiceRequest addCriticalServiceRequest) {
        List<CriticalService> criticalServiceList = criticalServiceRepository.findCritiCalServiceByServiceName(addCriticalServiceRequest.getServiceName());
        if(!criticalServiceList.isEmpty()){
            throw new AppException(ErrorCode.DUPlICATED_SERVICE,ErrorCode.DUPlICATED_SERVICE.getMessage());
        }
    }
}
