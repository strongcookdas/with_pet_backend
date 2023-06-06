package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.repository.custom.reservation.ReservationRepositoryCustom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
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

    @Query("select r from Reservation r where r.reservationStatus = :payedstatus and "
            + "function('datediff', r.checkIn, now()) < 3")
    Optional<List<Reservation>> findNeedRefundReservation(@Param("payedstatus") ReservationStatus paystatus);

    @Query("select r from Reservation r where r.user.id=:userId and r.reservationStatus=:status")
    List<Reservation> findReservationByStatus(@Param("userId") String userId, @Param("status") String status);

    @Modifying(clearAutomatically = true)
    @Query("update Reservation r set r.reservationStatus = :changestatus where r.reservationStatus = :waitstatus and "
            + "function('datediff', r.checkIn, now() ) < 7")
    void executeAutoCancel(@Param("changestatus") ReservationStatus changeStatus, @Param("waitstatus") ReservationStatus waitstatus);


    @Modifying(clearAutomatically = true)
    @Query("update Reservation r set r.reservationStatus = :donestatus where r.reservationStatus = :approvalstatus and "
            + "function('datediff', now(), r.checkOut) > 1")
    void executeAutoDone(@Param("donestatus") ReservationStatus doneStatus, @Param("approvalstatus") ReservationStatus approvalstatus);

    List<Reservation> findAllByPetSitterAndReservationStatus(PetSitter petSitter, ReservationStatus status);
    @Query("select r from Reservation r where r.tid=:tid")
    Optional<Reservation> findByTid(@Param("tid") String tid);

}
