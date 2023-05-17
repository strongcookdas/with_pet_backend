package com.ajou_nice.with_pet.repository.custom.reservation;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {

    List<Reservation> findAllByPetsitterAndMonthAndStatus(PetSitter petSitter, LocalDate month, List<ReservationStatus> list);

    List<Reservation> findAllByPetsitterAndMonth(PetSitter petSitter, LocalDate month);

}
