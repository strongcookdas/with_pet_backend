package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.repository.custom.reservation.ReservationRepositoryCustom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long>,
        ReservationRepositoryCustom {


    //펫시터 입장에서 유효 체크
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Boolean existsByCheckInBetweenAndPetSitterAndReservationStatusIn(LocalDateTime checkIn,
            LocalDateTime checkOut, PetSitter petSitter, List<ReservationStatus> list);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
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

    @Query("select r from Reservation r where r.petSitter=:petSitter")
    List<Reservation> findAllReservationByPetSitter(@Param("petSitter") PetSitter petSitter);

    @Modifying(clearAutomatically = true)
    @Query("update Reservation r set r.reservationStatus = :changestatus where r.reservationStatus = :waitstatus and "
            + "(function('datediff', now(), r.createdAt ) >= 1 or function('datediff', r.checkIn, now()) <= 1)")
    void executeAutoCancel(@Param("changestatus") ReservationStatus changeStatus, @Param("waitstatus") ReservationStatus waitstatus);


    @Modifying(clearAutomatically = true)
    @Query("update Reservation r set r.reservationStatus = :donestatus where r.reservationStatus = :usestatus and "
            + "function('datediff', now(), r.checkOut) >= 2")
    void executeAutoDone(@Param("donestatus") ReservationStatus doneStatus, @Param("usestatus") ReservationStatus usestatus);

    @Modifying(clearAutomatically = true)
    @Query("update Reservation r set r.reservationStatus = :useStatus where r.reservationStatus = :approvalStatus and "
            + "function('datediff', now(), r.checkIn) = 0")
    void executeAutoUse(@Param("useStatus") ReservationStatus useStatus, @Param("approvalStatus") ReservationStatus approvalStatus);

    @Query("select r from Reservation r where r.user =:user")
    Optional<List<Reservation>> findAllByUser(@Param("user") User user);


    List<Reservation> findAllByPetSitterAndReservationStatus(PetSitter petSitter, ReservationStatus status);

    @Query("select r from Reservation r where r.tid=:tid")
    Optional<Reservation> findByTid(@Param("tid") String tid);

    Boolean existsByDogAndReservationStatusIn(Dog dog,List<ReservationStatus> statuses);

    Boolean existsByUserAndDogInAndReservationStatusIn(User user,List<Dog> dogs,
            List<ReservationStatus> statuses);
}
