package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWithPetService is a Querydsl query type for WithPetService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithPetService extends EntityPathBase<WithPetService> {

    private static final long serialVersionUID = -1682792745L;

    public static final QWithPetService withPetService = new QWithPetService("withPetService");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    public final StringPath name = createString("name");

    public final StringPath service_Img = createString("service_Img");

    public QWithPetService(String variable) {
        super(WithPetService.class, forVariable(variable));
    }

    public QWithPetService(Path<? extends WithPetService> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWithPetService(PathMetadata metadata) {
        super(WithPetService.class, metadata);
    }

}

