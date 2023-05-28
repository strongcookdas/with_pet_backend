package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetSitter is a Querydsl query type for PetSitter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetSitter extends EntityPathBase<PetSitter> {

    private static final long serialVersionUID = -675494201L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetSitter petSitter = new QPetSitter("petSitter");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final EnumPath<com.ajou_nice.with_pet.enums.DogSize> availableDogSize = createEnum("availableDogSize", com.ajou_nice.with_pet.enums.DogSize.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final ListPath<PetSitterCriticalService, QPetSitterCriticalService> petSitterCriticalServiceList = this.<PetSitterCriticalService, QPetSitterCriticalService>createList("petSitterCriticalServiceList", PetSitterCriticalService.class, QPetSitterCriticalService.class, PathInits.DIRECT2);

    public final StringPath petSitterDetailAdr = createString("petSitterDetailAdr");

    public final ListPath<PetSitterHashTag, QPetSitterHashTag> petSitterHashTagList = this.<PetSitterHashTag, QPetSitterHashTag>createList("petSitterHashTagList", PetSitterHashTag.class, QPetSitterHashTag.class, PathInits.DIRECT2);

    public final ListPath<House, QHouse> petSitterHouseList = this.<House, QHouse>createList("petSitterHouseList", House.class, QHouse.class, PathInits.DIRECT2);

    public final StringPath petSitterLicenseImg = createString("petSitterLicenseImg");

    public final StringPath petSitterName = createString("petSitterName");

    public final StringPath petSitterPhone = createString("petSitterPhone");

    public final StringPath petSitterStreetAdr = createString("petSitterStreetAdr");

    public final ListPath<PetSitterWithPetService, QPetSitterWithPetService> petSitterWithPetServiceList = this.<PetSitterWithPetService, QPetSitterWithPetService>createList("petSitterWithPetServiceList", PetSitterWithPetService.class, QPetSitterWithPetService.class, PathInits.DIRECT2);

    public final StringPath petSitterZipCode = createString("petSitterZipCode");

    public final StringPath profileImg = createString("profileImg");

    public final NumberPath<Integer> report_count = createNumber("report_count", Integer.class);

    public final NumberPath<Integer> review_count = createNumber("review_count", Integer.class);

    public final NumberPath<Double> star_rate = createNumber("star_rate", Double.class);

    public final QUser user;

    public final BooleanPath valid = createBoolean("valid");

    public QPetSitter(String variable) {
        this(PetSitter.class, forVariable(variable), INITS);
    }

    public QPetSitter(Path<? extends PetSitter> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetSitter(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetSitter(PathMetadata metadata, PathInits inits) {
        this(PetSitter.class, metadata, inits);
    }

    public QPetSitter(Class<? extends PetSitter> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

