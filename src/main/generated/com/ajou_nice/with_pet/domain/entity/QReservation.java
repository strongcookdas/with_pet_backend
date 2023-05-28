package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = -1652747055L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> checkIn = createDateTime("checkIn", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> checkOut = createDateTime("checkOut", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

<<<<<<< HEAD
=======
    public final StringPath criticalServiceName = createString("criticalServiceName");

    public final NumberPath<Integer> criticalServicePrice = createNumber("criticalServicePrice", Integer.class);

>>>>>>> developer
    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final QDog dog;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

<<<<<<< HEAD
    public final QPay pay;

    public final QPetSitter petSitter;

    public final QPetSitterCriticalService petSitterCriticalService;

    public final NumberPath<Long> reservationId = createNumber("reservationId", Long.class);

    public final EnumPath<com.ajou_nice.with_pet.enums.ReservationStatus> reservationStatus = createEnum("reservationStatus", com.ajou_nice.with_pet.enums.ReservationStatus.class);

=======
    public final QPetSitter petSitter;

    public final NumberPath<Long> petSitterCriticalServiceId = createNumber("petSitterCriticalServiceId", Long.class);

    public final NumberPath<Long> reservationId = createNumber("reservationId", Long.class);

    public final ListPath<ReservationPetSitterService, QReservationPetSitterService> reservationPetSitterServiceList = this.<ReservationPetSitterService, QReservationPetSitterService>createList("reservationPetSitterServiceList", ReservationPetSitterService.class, QReservationPetSitterService.class, PathInits.DIRECT2);

    public final EnumPath<com.ajou_nice.with_pet.enums.ReservationStatus> reservationStatus = createEnum("reservationStatus", com.ajou_nice.with_pet.enums.ReservationStatus.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

>>>>>>> developer
    public final QUser user;

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservation(PathMetadata metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dog = inits.isInitialized("dog") ? new QDog(forProperty("dog"), inits.get("dog")) : null;
<<<<<<< HEAD
        this.pay = inits.isInitialized("pay") ? new QPay(forProperty("pay"), inits.get("pay")) : null;
        this.petSitter = inits.isInitialized("petSitter") ? new QPetSitter(forProperty("petSitter"), inits.get("petSitter")) : null;
        this.petSitterCriticalService = inits.isInitialized("petSitterCriticalService") ? new QPetSitterCriticalService(forProperty("petSitterCriticalService"), inits.get("petSitterCriticalService")) : null;
=======
        this.petSitter = inits.isInitialized("petSitter") ? new QPetSitter(forProperty("petSitter"), inits.get("petSitter")) : null;
>>>>>>> developer
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

