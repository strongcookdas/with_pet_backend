package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = -994807442L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final NumberPath<Integer> allMessageCount = createNumber("allMessageCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = createDateTime("lastModifiedTime", java.time.LocalDateTime.class);

    public final QUser me;

    public final DateTimePath<java.time.LocalDateTime> myLastShowTime = createDateTime("myLastShowTime", java.time.LocalDateTime.class);

    public final QUser other;

    public final NumberPath<Long> roomId = createNumber("roomId", Long.class);

    public final ListPath<ChatMessage, QChatMessage> roomMessages = this.<ChatMessage, QChatMessage>createList("roomMessages", ChatMessage.class, QChatMessage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> showMessageCount = createNumber("showMessageCount", Integer.class);

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.me = inits.isInitialized("me") ? new QUser(forProperty("me"), inits.get("me")) : null;
        this.other = inits.isInitialized("other") ? new QUser(forProperty("other"), inits.get("other")) : null;
    }

}

