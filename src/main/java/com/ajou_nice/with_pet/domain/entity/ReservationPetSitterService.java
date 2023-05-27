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
public class ReservationPetSitterService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private Integer price;
    private String serviceName;

    public static ReservationPetSitterService of(Reservation reservation,
            PetSitterWithPetService withPetService) {
        return ReservationPetSitterService.builder()
                .reservation(reservation)
                .price(withPetService.getPrice())
                .serviceName(withPetService.getWithPetService().getName())
                .build();
    }
}
