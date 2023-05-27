package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetSitterHashTag is a Querydsl query type for PetSitterHashTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetSitterHashTag extends EntityPathBase<PetSitterHashTag> {

    private static final long serialVersionUID = -1303496987L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetSitterHashTag petSitterHashTag = new QPetSitterHashTag("petSitterHashTag");

    public final StringPath hashTagName = createString("hashTagName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPetSitter petSitter;

    public QPetSitterHashTag(String variable) {
        this(PetSitterHashTag.class, forVariable(variable), INITS);
    }

    public QPetSitterHashTag(Path<? extends PetSitterHashTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetSitterHashTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetSitterHashTag(PathMetadata metadata, PathInits inits) {
        this(PetSitterHashTag.class, metadata, inits);
    }

    public QPetSitterHashTag(Class<? extends PetSitterHashTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.petSitter = inits.isInitialized("petSitter") ? new QPetSitter(forProperty("petSitter"), inits.get("petSitter")) : null;
    }

}

