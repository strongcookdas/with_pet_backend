package com.ajou_nice.with_pet.petsitter.repository;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitterCriticalService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PetSitterCriticalServiceRepository extends
        JpaRepository<PetSitterCriticalService, Long> {

    @Query("select s from PetSitterCriticalService s where s.petSitter.id=:petSitterId")
    List<PetSitterCriticalService> findAllByPetSitterInQuery(
            @Param("petSitterId") Long petSitterId);

    @Modifying
    @Transactional
    @Query("delete from PetSitterCriticalService c where c.petSitter.id=:petSitterId")
    void deleteAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);

    Optional<PetSitterCriticalService> findByPetSitterAndCriticalService_ServiceName(
            PetSitter petSitter, String serviceName);

    List<PetSitterCriticalService> findAllByPetSitterId(Long petSitterId);

}
