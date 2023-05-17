package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.CriticalService;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CriticalServiceRepository extends JpaRepository<CriticalService, Long> {

	@Query("select c from CriticalService c where c.serviceName=:serviceName")
	Optional<CriticalService> findCritiCalServiceByServiceName(@Param("serviceName") String serviceName);
}
