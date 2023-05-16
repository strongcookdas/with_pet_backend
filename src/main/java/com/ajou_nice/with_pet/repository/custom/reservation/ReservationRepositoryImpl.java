package com.ajou_nice.with_pet.repository.custom.reservation;

import static com.ajou_nice.with_pet.domain.entity.QReservation.reservation;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
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
    public List<Reservation> findAllByPetsitterAndMonth(PetSitter petSitter, LocalDate month,
            List<ReservationStatus> list) {

        List<Reservation> reservations = queryFactory.selectFrom(reservation)
                .where(containPetsitter(petSitter), getMonthReservationCheckIn(month),
                        getMonthReservationCheckOut(month), compareReservationStatus(list))
                .orderBy(reservation.createdAt.desc())
                .fetch();
        return reservations;
    }

    private BooleanExpression containPetsitter(PetSitter petSitter) {
        return reservation.petSitter.id.eq(petSitter.getId());
    }

    private BooleanExpression getMonthReservationCheckIn(LocalDate month) {
        return reservation.checkIn.between(month.withDayOfMonth(1).atStartOfDay(),
                month.withDayOfMonth(month.lengthOfMonth()).atStartOfDay());
    }

    private BooleanExpression getMonthReservationCheckOut(LocalDate month) {
        return reservation.checkOut.between(month.withDayOfMonth(1).atStartOfDay(),
                month.withDayOfMonth(month.lengthOfMonth()).atStartOfDay());
    }

    private BooleanExpression compareReservationStatus(List<ReservationStatus> list) {
        return reservation.reservationStatus.in(list);
    }
}
