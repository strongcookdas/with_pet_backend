package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetSitterApplicant is a Querydsl query type for PetSitterApplicant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetSitterApplicant extends EntityPathBase<PetSitterApplicant> {

    private static final long serialVersionUID = -919066629L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetSitterApplicant petSitterApplicant = new QPetSitterApplicant("petSitterApplicant");

    public final StringPath animal_career = createString("animal_career");

    public final EnumPath<com.ajou_nice.with_pet.enums.ApplicantStatus> applicantStatus = createEnum("applicantStatus", com.ajou_nice.with_pet.enums.ApplicantStatus.class);

    public final StringPath care_experience = createString("care_experience");

    public final BooleanPath having_with_pet = createBoolean("having_with_pet");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath identification = createString("identification");

    public final BooleanPath is_smoking = createBoolean("is_smoking");

    public final StringPath license_img = createString("license_img");

    public final StringPath motivate = createString("motivate");

    public final StringPath petsitter_career = createString("petsitter_career");

    public final QUser user;

    public QPetSitterApplicant(String variable) {
        this(PetSitterApplicant.class, forVariable(variable), INITS);
    }

    public QPetSitterApplicant(Path<? extends PetSitterApplicant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetSitterApplicant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetSitterApplicant(PathMetadata metadata, PathInits inits) {
        this(PetSitterApplicant.class, metadata, inits);
    }

    public QPetSitterApplicant(Class<? extends PetSitterApplicant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

