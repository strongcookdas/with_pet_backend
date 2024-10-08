package com.ajou_nice.with_pet.reservation.service;

import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationValidationService {

    private final ReservationRepository reservationRepository;

    public Reservation reservationValidationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new AppException(
                ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage()));
    }
}
