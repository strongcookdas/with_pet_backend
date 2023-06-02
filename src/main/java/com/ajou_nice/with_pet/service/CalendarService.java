package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.calendar.PetSitterSideBarResponse;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PetSitterRepository petSitterRepository;

    public PetSitterSideBarResponse getPetSitterSideBarInfo(String userId, String month) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        PetSitter petSitter = petSitterRepository.findByUser(user).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });

        List<Reservation> useReservation = reservationRepository.getPetsitterSideBarInfo(petSitter,
                LocalDate.parse(month + "-01"), ReservationStatus.APPROVAL);
        List<Reservation> waitReservation = reservationRepository.findAllByPetSitterAndReservationStatus(
                petSitter, ReservationStatus.WAIT);
        List<Reservation> doneReservation = reservationRepository.getPetsitterSideBarInfo(petSitter,
                LocalDate.parse(month + "-01"), ReservationStatus.DONE);

        int cost = 0;
        for (Reservation reservation : doneReservation) {
            cost += reservation.getTotalPrice();
        }

        return PetSitterSideBarResponse.of(useReservation, waitReservation, doneReservation, cost);
    }
}