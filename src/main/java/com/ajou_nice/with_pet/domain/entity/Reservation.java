package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @NotNull
    private LocalDateTime checkIn;

    @NotNull
    private LocalDateTime checkOut;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    public static Reservation of(ReservationRequest reservationRequest, User user, Dog dog,
            PetSitter petSitter) {
        return Reservation.builder()
                .user(user)
                .dog(dog)
                .petSitter(petSitter)
                .checkIn(reservationRequest.getCheckIn())
                .checkOut(reservationRequest.getCheckOut())
                .reservationStatus(ReservationStatus.WAIT)
                .build();
    }
}
