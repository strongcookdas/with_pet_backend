package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservationPetSitterService is a Querydsl query type for ReservationPetSitterService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservationPetSitterService extends EntityPathBase<ReservationPetSitterService> {

    private static final long serialVersionUID = -1389672764L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservationPetSitterService reservationPetSitterService = new QReservationPetSitterService("reservationPetSitterService");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QReservation reservation;

    public final StringPath serviceName = createString("serviceName");

    public QReservationPetSitterService(String variable) {
        this(ReservationPetSitterService.class, forVariable(variable), INITS);
    }

    public QReservationPetSitterService(Path<? extends ReservationPetSitterService> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservationPetSitterService(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservationPetSitterService(PathMetadata metadata, PathInits inits) {
        this(ReservationPetSitterService.class, metadata, inits);
    }

    public QReservationPetSitterService(Class<? extends ReservationPetSitterService> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reservation = inits.isInitialized("reservation") ? new QReservation(forProperty("reservation"), inits.get("reservation")) : null;
    }

}

