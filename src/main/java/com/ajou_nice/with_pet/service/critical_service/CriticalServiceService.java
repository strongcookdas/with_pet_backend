package com.ajou_nice.with_pet.service.critical_service;

import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceResponse;
import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CriticalServiceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriticalServiceService {

    private final CriticalServiceRepository criticalServiceRepository;

    /**
     * 필수 서비스 조회
     **/
    public CriticalService getCriticalService(Long criticalServiceId) {
        CriticalService criticalService = criticalServiceRepository.findById(criticalServiceId)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.CRITICAL_SERVICE_NOT_FOUND,
                            ErrorCode.CRITICAL_SERVICE_NOT_FOUND.getMessage());
                });
        return criticalService;
    }

    /**
     * 필수 서비스 리스트 조회
     **/
    public List<CriticalService> getCriticalServices() {
        return criticalServiceRepository.findAll();
    }
}
