package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogId")
    private Dog dog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationPetSitterService> reservationPetSitterServiceList;

    @NotNull
    private LocalDateTime checkIn;

    @NotNull
    private LocalDateTime checkOut;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private Long petSitterCriticalServiceId;
    private String criticalServiceName;
    private Integer criticalServicePrice;
    private Integer totalPrice;

    //결제 건의 고유번호
    private String tid;


    public static Reservation of(ReservationRequest reservationRequest, User user, Dog dog,
                                 PetSitter petSitter, PetSitterCriticalService petSitterCriticalService) {
        return Reservation.builder()
                .user(user)
                .dog(dog)
                .petSitter(petSitter)
                .checkIn(reservationRequest.getCheckIn())
                .checkOut(reservationRequest.getCheckOut())
                .reservationStatus(ReservationStatus.WAIT)
                .petSitterCriticalServiceId(petSitterCriticalService.getId())
                .criticalServiceName(petSitterCriticalService.getCriticalService().getServiceName())
                .criticalServicePrice(petSitterCriticalService.getPrice())
                .totalPrice(0)
                .build();
    }

    public static Reservation forSchedulerTest(LocalDateTime checkIn, LocalDateTime checkOut){
        return Reservation.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .reservationStatus(ReservationStatus.WAIT)
                .build();
    }

    public void updateForTest(String criticalServiceName, int criticalServicePrice){
        this.criticalServiceName = criticalServiceName;
        this.criticalServicePrice = criticalServicePrice;
    }

    public static Reservation forSimpleTest(LocalDateTime checkIn, LocalDateTime checkOut, User user, PetSitter petSitter, int totalCost){
        return Reservation.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .user(user)
                .petSitter(petSitter)
                .totalPrice(totalCost)
                .build();
    }

    public static Reservation forSimpleTest(LocalDateTime checkIn, LocalDateTime checkOut, User user, PetSitter petSitter, int totalCost, ReservationStatus status, Dog dog){
        return Reservation.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .user(user)
                .petSitter(petSitter)
                .totalPrice(totalCost)
                .reservationStatus(status)
                .dog(dog)
                .build();
    }

    public static Reservation forSimpleTest(LocalDateTime checkIn, LocalDateTime checkOut, User user,
            PetSitter petSitter, int totalCost, ReservationStatus status, Dog dog, String criticalServiceName, int criticalServicePrice){
        return Reservation.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .user(user)
                .petSitter(petSitter)
                .totalPrice(totalCost)
                .reservationStatus(status)
                .dog(dog)
                .criticalServiceName(criticalServiceName)
                .criticalServicePrice(criticalServicePrice)
                .build();
    }

    public void updateTid(String tid){
        this.tid = tid;
    }

    public void approvePay(ReservationStatus reservationStatus){
        this.reservationStatus = reservationStatus;
    }

    public void updateReservationServices(List<ReservationPetSitterService> petSitterServices){
        this.reservationPetSitterServiceList = petSitterServices;
    }

    public void updateTotalPrice(int price){
        this.totalPrice = price;
    }

    public void updateStatus(String status) {
        this.reservationStatus = ReservationStatus.valueOf(status);
    }
}
