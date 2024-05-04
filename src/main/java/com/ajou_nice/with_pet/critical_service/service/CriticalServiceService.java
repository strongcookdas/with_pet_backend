package com.ajou_nice.with_pet.critical_service.service;

import com.ajou_nice.with_pet.critical_service.dto.CriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.entity.CriticalService;
import com.ajou_nice.with_pet.critical_service.repository.CriticalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriticalServiceService {
    private final CriticalServiceRepository criticalServiceRepository;
    public List<CriticalServiceResponse> showCriticalServices() {
        List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();
        return CriticalServiceResponse.toList(criticalServiceList);
    }
}
