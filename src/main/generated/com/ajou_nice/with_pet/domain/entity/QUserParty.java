package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserParty is a Querydsl query type for UserParty
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserParty extends EntityPathBase<UserParty> {

    private static final long serialVersionUID = -2057096160L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserParty userParty = new QUserParty("userParty");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final QParty party;

    public final QUser user;

    public final NumberPath<Long> userPartyId = createNumber("userPartyId", Long.class);

    public QUserParty(String variable) {
        this(UserParty.class, forVariable(variable), INITS);
    }

    public QUserParty(Path<? extends UserParty> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserParty(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserParty(PathMetadata metadata, PathInits inits) {
        this(UserParty.class, metadata, inits);
    }

    public QUserParty(Class<? extends UserParty> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.party = inits.isInitialized("party") ? new QParty(forProperty("party"), inits.get("party")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

