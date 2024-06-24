package com.ajou_nice.with_pet.reservation.repository.custom;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {

    List<Reservation> findAllByPetSitterAndMonthAndStatus(PetSitter petSitter, LocalDate month, List<ReservationStatus> list);

    List<Reservation> findAllByPetsitterAndMonth(PetSitter petSitter, LocalDate month);

    List<Reservation> getPetsitterSideBarInfo(PetSitter petSitter, LocalDate month, ReservationStatus reservationStatus);


}
