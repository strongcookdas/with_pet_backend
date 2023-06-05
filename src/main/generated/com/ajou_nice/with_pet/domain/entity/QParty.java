package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QParty is a Querydsl query type for Party
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParty extends EntityPathBase<Party> {

    private static final long serialVersionUID = 1264473003L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QParty party = new QParty("party");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath name = createString("name");

    public final NumberPath<Long> partyId = createNumber("partyId", Long.class);

    public final StringPath partyIsbn = createString("partyIsbn");

    public final QUser user;

    public final ListPath<UserParty, QUserParty> userPartyList = this.<UserParty, QUserParty>createList("userPartyList", UserParty.class, QUserParty.class, PathInits.DIRECT2);

    public QParty(String variable) {
        this(Party.class, forVariable(variable), INITS);
    }

    public QParty(Path<? extends Party> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParty(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParty(PathMetadata metadata, PathInits inits) {
        this(Party.class, metadata, inits);
    }

    public QParty(Class<? extends Party> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

