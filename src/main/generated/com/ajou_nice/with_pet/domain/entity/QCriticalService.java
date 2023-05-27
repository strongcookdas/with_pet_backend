package com.ajou_nice.with_pet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCriticalService is a Querydsl query type for CriticalService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCriticalService extends EntityPathBase<CriticalService> {

    private static final long serialVersionUID = -636113765L;

    public static final QCriticalService criticalService = new QCriticalService("criticalService");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    public final StringPath serviceImg = createString("serviceImg");

    public final StringPath serviceName = createString("serviceName");

    public QCriticalService(String variable) {
        super(CriticalService.class, forVariable(variable));
    }

    public QCriticalService(Path<? extends CriticalService> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCriticalService(PathMetadata metadata) {
        super(CriticalService.class, metadata);
    }

}

