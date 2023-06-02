package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
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

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "dogId")
    private Dog dog;

    @ManyToOne
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationPetSitterService> reservationPetSitterServiceList;

    @OneToOne(mappedBy = "reservation")
    private Pay pay;

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

    public void updatePay(Pay pay){
        this.pay = pay;
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
