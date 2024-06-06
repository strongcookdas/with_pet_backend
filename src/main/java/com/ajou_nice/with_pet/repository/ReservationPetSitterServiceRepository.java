package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.reservation.model.entity.ReservationPetSitterService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationPetSitterServiceRepository extends JpaRepository<ReservationPetSitterService, Long> {

}
