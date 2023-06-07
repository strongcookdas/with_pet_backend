package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPay is a Querydsl query type for Pay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPay extends EntityPathBase<Pay> {

    private static final long serialVersionUID = -61253971L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPay pay = new QPay("pay");

    public final StringPath approved_at = createString("approved_at");

    public final StringPath canceled_at = createString("canceled_at");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath item_name = createString("item_name");

    public final NumberPath<Integer> pay_amount = createNumber("pay_amount", Integer.class);

    public final EnumPath<com.ajou_nice.with_pet.enums.PayStatus> payStatus = createEnum("payStatus", com.ajou_nice.with_pet.enums.PayStatus.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final NumberPath<Integer> refund_amount = createNumber("refund_amount", Integer.class);

    public final QReservation reservation;

    public final StringPath tid = createString("tid");

    public QPay(String variable) {
        this(Pay.class, forVariable(variable), INITS);
    }

    public QPay(Path<? extends Pay> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPay(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPay(PathMetadata metadata, PathInits inits) {
        this(Pay.class, metadata, inits);
    }

    public QPay(Class<? extends Pay> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reservation = inits.isInitialized("reservation") ? new QReservation(forProperty("reservation"), inits.get("reservation")) : null;
    }

}

