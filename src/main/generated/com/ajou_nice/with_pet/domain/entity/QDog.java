package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDog is a Querydsl query type for Dog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDog extends EntityPathBase<Dog> {

    private static final long serialVersionUID = -61265087L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDog dog = new QDog("dog");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Double> affectionTemperature = createNumber("affectionTemperature", Double.class);

    public final DatePath<java.time.LocalDate> birth = createDate("birth", java.time.LocalDate.class);

    public final StringPath breed = createString("breed");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> dogId = createNumber("dogId", Long.class);

    public final EnumPath<com.ajou_nice.with_pet.enums.DogSize> dogSize = createEnum("dogSize", com.ajou_nice.with_pet.enums.DogSize.class);

    public final StringPath gender = createString("gender");

    public final StringPath isbn = createString("isbn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath name = createString("name");

    public final BooleanPath neutralization = createBoolean("neutralization");

    public final QParty party;

    public final StringPath profile_img = createString("profile_img");

    public final NumberPath<Integer> socializationDegree = createNumber("socializationDegree", Integer.class);

    public final NumberPath<Double> socializationTemperature = createNumber("socializationTemperature", Double.class);

    public final NumberPath<Float> weight = createNumber("weight", Float.class);

    public QDog(String variable) {
        this(Dog.class, forVariable(variable), INITS);
    }

    public QDog(Path<? extends Dog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDog(PathMetadata metadata, PathInits inits) {
        this(Dog.class, metadata, inits);
    }

    public QDog(Class<? extends Dog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.party = inits.isInitialized("party") ? new QParty(forProperty("party"), inits.get("party")) : null;
    }

}

