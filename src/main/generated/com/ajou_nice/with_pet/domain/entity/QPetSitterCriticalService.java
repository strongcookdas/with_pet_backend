package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetSitterCriticalService is a Querydsl query type for PetSitterCriticalService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetSitterCriticalService extends EntityPathBase<PetSitterCriticalService> {

    private static final long serialVersionUID = 818712655L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetSitterCriticalService petSitterCriticalService = new QPetSitterCriticalService("petSitterCriticalService");

    public final QCriticalService criticalService;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPetSitter petSitter;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public QPetSitterCriticalService(String variable) {
        this(PetSitterCriticalService.class, forVariable(variable), INITS);
    }

    public QPetSitterCriticalService(Path<? extends PetSitterCriticalService> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetSitterCriticalService(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetSitterCriticalService(PathMetadata metadata, PathInits inits) {
        this(PetSitterCriticalService.class, metadata, inits);
    }

    public QPetSitterCriticalService(Class<? extends PetSitterCriticalService> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.criticalService = inits.isInitialized("criticalService") ? new QCriticalService(forProperty("criticalService")) : null;
        this.petSitter = inits.isInitialized("petSitter") ? new QPetSitter(forProperty("petSitter"), inits.get("petSitter")) : null;
    }

}

