package com.ajou_nice.with_pet.repository.custom;

import static com.ajou_nice.with_pet.domain.entity.QPetSitter.petSitter;

import com.ajou_nice.with_pet.domain.entity.QPetSitter;
import com.ajou_nice.with_pet.domain.entity.QPetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.QPetSitterWithPetService;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class PetSitterRepositoryImpl extends QuerydslRepositorySupport implements
        PetSitterRespositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public PetSitterRepositoryImpl() {
        super(PetSitter.class);
    }

    @Override
    public Page<PetSitter> searchPage(Pageable pageable, String dogSize, String service,
            String address) {

        QPetSitterCriticalService petSitterCriticalService = new QPetSitterCriticalService("c");
        QPetSitterWithPetService petSitterWithPetService = new QPetSitterWithPetService("w");

        List<PetSitter> petSitters = queryFactory.selectFrom(petSitter)
                .where(petSitter.valid.eq(true),
                        containsDogSize(petSitterCriticalService, petSitter, dogSize),
                        containService(petSitterWithPetService, petSitter, service),
                        containAddress(address))
                .fetch();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), petSitters.size());
        return new PageImpl<PetSitter>(petSitters.subList(start, end), pageable, petSitters.size());
    }

    private BooleanExpression containsDogSize(QPetSitterCriticalService petSitterCriticalService,
            QPetSitter petSitter,
            String dogSize) {
        if (dogSize == null || dogSize.isEmpty()) {
            return null;
        }
        List<Long> petSitterIdList = queryFactory.select(petSitterCriticalService.petSitter.id)
                .from(petSitterCriticalService)
                .innerJoin(petSitterCriticalService.petSitter, petSitter)
                .where(petSitterCriticalService.criticalService.serviceName.eq(dogSize)).fetch();
        return petSitter.id.in(petSitterIdList);
    }

    private BooleanExpression containService(QPetSitterWithPetService petSitterWithPetService,
            QPetSitter petSitter,
            String service) {
        if (service == null || service.isEmpty()) {
            return null;
        }
        List<Long> petSitterIdList = queryFactory.select(petSitterWithPetService.petSitter.id)
                .from(petSitterWithPetService)
                .innerJoin(petSitterWithPetService.petSitter, petSitter).fetchJoin()
                .where(petSitterWithPetService.withPetService.name.eq(service)).fetch();
        return petSitter.id.in(petSitterIdList);
    }

    private BooleanExpression containAddress(String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }
        log.info("======================= address : {} ========================", address);
        List<Long> petSitterIdList = queryFactory.select(petSitter.id).from(petSitter)
                .where(petSitter.petSitterStreetAdr.contains(address)).fetch();
        log.info("======================= petSitterIdList : {} ========================",
                petSitterIdList);
        return petSitter.petSitterStreetAdr.contains(address);
    }

}
