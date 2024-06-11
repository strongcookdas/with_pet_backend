package com.ajou_nice.with_pet.reservation.repository.custom;

import static com.ajou_nice.with_pet.reservation.model.entity.QReservation.reservation;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ReservationRepositoryImpl extends QuerydslRepositorySupport implements
        ReservationRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public ReservationRepositoryImpl() {
        super(Reservation.class);
    }

    @Override
    public List<Reservation> findAllByPetsitterAndMonthAndStatus(PetSitter petSitter,
            LocalDate month,
            List<ReservationStatus> list) {

        List<Reservation> reservations = queryFactory.selectFrom(reservation)
                .where(containPetsitter(petSitter), getMonthReservationCheckIn(month), compareReservationStatus(list))
                .orderBy(reservation.reservationCheckIn.asc())
                .fetch();
        return reservations;
    }

    @Override
    public List<Reservation> findAllByPetsitterAndMonth(PetSitter petSitter, LocalDate month) {
        List<Reservation> reservations = queryFactory.selectFrom(reservation)
                .where(containPetsitter(petSitter), getMonthReservationCheckIn(month))
                .orderBy(reservation.reservationCheckIn.asc()).fetch();
        return reservations;
    }

    @Override
    public List<Reservation> getPetsitterSideBarInfo(PetSitter petSitter, LocalDate month,
            ReservationStatus reservationStatus) {
        List<Reservation> reservations = queryFactory.selectFrom(reservation)
                .where(containPetsitter(petSitter), getMonthReservationCheckIn(month),
                        compareReservationStatus(reservationStatus))
                .orderBy(reservation.reservationCheckIn.asc())
                .fetch();
        return reservations;
    }

    private BooleanExpression containPetsitter(PetSitter petSitter) {
        return reservation.petSitter.id.eq(petSitter.getId());
    }

    private BooleanExpression getMonthReservationCheckIn(LocalDate month) {
        return reservation.reservationCheckIn.between(month.withDayOfMonth(1).atStartOfDay(),
                month.withDayOfMonth(month.lengthOfMonth()).atStartOfDay());
    }

    private BooleanExpression getMonthReservationCheckOut(LocalDate month) {
        return reservation.reservationCheckOut.between(month.withDayOfMonth(1).atStartOfDay(),
                month.withDayOfMonth(month.lengthOfMonth()).atStartOfDay());
    }

    private BooleanExpression compareReservationStatus(List<ReservationStatus> list) {
        return reservation.reservationStatus.in(list);
    }

    private BooleanExpression compareReservationStatus(ReservationStatus reservationStatus) {
        return reservation.reservationStatus.eq(reservationStatus);
    }
}
