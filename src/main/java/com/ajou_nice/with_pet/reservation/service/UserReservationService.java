package com.ajou_nice.with_pet.reservation.service;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationGetInfosResponse;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.user.service.UserValidationService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReservationService {

    private final UserValidationService userValidationService;

    private final ReservationRepository reservationRepository;

    public UserReservationGetInfosResponse getUserReservations(String email) {
        User user = userValidationService.userValidationByEmail(email);
        List<Reservation> userReservations = reservationRepository.findAllByUser(user).get();

        return filterReservationsByStatus(userReservations);
    }

    public UserReservationGetInfosResponse filterReservationsByStatus(List<Reservation> userReservations) {
        List<Reservation> waitReservations = filterByStatus(userReservations, ReservationStatus.WAIT);
        List<Reservation> payedReservations = filterByStatus(userReservations, ReservationStatus.PAYED);
        List<Reservation> approveReservations = filterByStatus(userReservations, ReservationStatus.APPROVAL);
        List<Reservation> useReservations = filterByStatus(userReservations, ReservationStatus.USE);
        List<Reservation> doneReservations = filterByStatus(userReservations, ReservationStatus.DONE);

        return UserReservationGetInfosResponse.of(waitReservations, payedReservations, approveReservations,
                useReservations, doneReservations);
    }

    private List<Reservation> filterByStatus(List<Reservation> reservations, ReservationStatus status) {
        return reservations.stream()
                .filter(reservation -> reservation.getReservationStatus().equals(status))
                .collect(Collectors.toList());
    }
}
