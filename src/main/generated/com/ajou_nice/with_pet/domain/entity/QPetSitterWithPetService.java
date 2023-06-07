package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetSitterWithPetService is a Querydsl query type for PetSitterWithPetService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetSitterWithPetService extends EntityPathBase<PetSitterWithPetService> {

    private static final long serialVersionUID = 2104915107L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetSitterWithPetService petSitterWithPetService = new QPetSitterWithPetService("petSitterWithPetService");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPetSitter petSitter;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QWithPetService withPetService;

    public QPetSitterWithPetService(String variable) {
        this(PetSitterWithPetService.class, forVariable(variable), INITS);
    }

    public QPetSitterWithPetService(Path<? extends PetSitterWithPetService> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetSitterWithPetService(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetSitterWithPetService(PathMetadata metadata, PathInits inits) {
        this(PetSitterWithPetService.class, metadata, inits);
    }

    public QPetSitterWithPetService(Class<? extends PetSitterWithPetService> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.petSitter = inits.isInitialized("petSitter") ? new QPetSitter(forProperty("petSitter"), inits.get("petSitter")) : null;
        this.withPetService = inits.isInitialized("withPetService") ? new QWithPetService(forProperty("withPetService")) : null;
    }

}

