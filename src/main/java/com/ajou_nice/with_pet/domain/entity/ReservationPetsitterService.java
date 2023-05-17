package com.ajou_nice.with_pet.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "reservation_service")
public class ReservationPetsitterService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "petsitter_service_id", nullable = false)
    private PetSitterWithPetService withPetService;

    public static ReservationPetsitterService of(Reservation reservation,
            PetSitterWithPetService withPetService) {
        return ReservationPetsitterService.builder()
                .reservation(reservation)
                .withPetService(withPetService)
                .build();
    }
}
