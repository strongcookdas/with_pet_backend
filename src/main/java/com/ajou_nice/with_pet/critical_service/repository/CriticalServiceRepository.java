package com.ajou_nice.with_pet.critical_service.repository;

import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CriticalServiceRepository extends JpaRepository<CriticalService, Long> {

	@Query("select c from CriticalService c where c.serviceName=:serviceName")
	List<CriticalService> findCritiCalServiceByServiceName(@Param("serviceName") String serviceName);
}
