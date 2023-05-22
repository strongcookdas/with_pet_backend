package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservationPetsitterService is a Querydsl query type for ReservationPetsitterService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservationPetsitterService extends EntityPathBase<ReservationPetsitterService> {

    private static final long serialVersionUID = 2106332900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservationPetsitterService reservationPetsitterService = new QReservationPetsitterService("reservationPetsitterService");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReservation reservation;

    public final QPetSitterWithPetService withPetService;

    public QReservationPetsitterService(String variable) {
        this(ReservationPetsitterService.class, forVariable(variable), INITS);
    }

    public QReservationPetsitterService(Path<? extends ReservationPetsitterService> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservationPetsitterService(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservationPetsitterService(PathMetadata metadata, PathInits inits) {
        this(ReservationPetsitterService.class, metadata, inits);
    }

    public QReservationPetsitterService(Class<? extends ReservationPetsitterService> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reservation = inits.isInitialized("reservation") ? new QReservation(forProperty("reservation"), inits.get("reservation")) : null;
        this.withPetService = inits.isInitialized("withPetService") ? new QPetSitterWithPetService(forProperty("withPetService"), inits.get("withPetService")) : null;
    }

}

