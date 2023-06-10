package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1898707354L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final com.ajou_nice.with_pet.domain.entity.embedded.QAddress address;

    public final StringPath animalCareer = createString("animalCareer");

    public final NumberPath<Integer> applicantCount = createNumber("applicantCount", Integer.class);

    public final EnumPath<com.ajou_nice.with_pet.enums.ApplicantStatus> applicantStatus = createEnum("applicantStatus", com.ajou_nice.with_pet.enums.ApplicantStatus.class);

    public final StringPath careExperience = createString("careExperience");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    public final BooleanPath havingWithPet = createBoolean("havingWithPet");

    public final StringPath id = createString("id");

    public final StringPath identification = createString("identification");

    public final BooleanPath isSmoking = createBoolean("isSmoking");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath licenseImg = createString("licenseImg");

    public final StringPath motivate = createString("motivate");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> partyCount = createNumber("partyCount", Integer.class);

    public final StringPath password = createString("password");

    public final StringPath petSitterCareer = createString("petSitterCareer");

    public final StringPath phone = createString("phone");

    public final StringPath profileImg = createString("profileImg");

    public final EnumPath<com.ajou_nice.with_pet.enums.UserRole> role = createEnum("role", com.ajou_nice.with_pet.enums.UserRole.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.ajou_nice.with_pet.domain.entity.embedded.QAddress(forProperty("address")) : null;
    }

}

