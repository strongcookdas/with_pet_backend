package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.repository.custom.reservation.ReservationRepositoryCustom;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long>,
        ReservationRepositoryCustom {

    //펫시터 입장에서 유효 체크
    Boolean existsByCheckInBetweenAndPetSitterAndReservationStatusIn(LocalDateTime checkIn,
            LocalDateTime checkOut, PetSitter petSitter, List<ReservationStatus> list);

    Boolean existsByCheckOutBetweenAndPetSitterAndReservationStatusIn(LocalDateTime checkIn,
            LocalDateTime checkOut, PetSitter petSitter, List<ReservationStatus> list);

    //반려인 입장에서 유효 체크
    Boolean existsByCheckInBetweenAndDogAndReservationStatusIn(LocalDateTime checkIn,
            LocalDateTime checkOut, Dog dog, List<ReservationStatus> reservationStatuses);

    Boolean existsByCheckOutBetweenAndDogAndReservationStatusIn(LocalDateTime checkIn,
            LocalDateTime checkOut, Dog dog, List<ReservationStatus> reservationStatuses);

    @Query("select r from Reservation r where r.user.id=:userId and r.reservationStatus=:status")
    List<Reservation> findReservationByStatus(@Param("userId") String userId, @Param("status") String status);

    List<Reservation> findAllByPetSitterAndReservationStatus(PetSitter petSitter, ReservationStatus status);
}
