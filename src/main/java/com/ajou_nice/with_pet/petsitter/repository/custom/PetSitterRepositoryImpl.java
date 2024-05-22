package com.ajou_nice.with_pet.petsitter.repository.custom;

import com.ajou_nice.with_pet.critical_service.model.entity.QPetSitterCriticalService;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.petsitter.model.entity.QPetSitter;
import com.ajou_nice.with_pet.withpet_service.model.entity.QPetSitterWithPetService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.ajou_nice.with_pet.petsitter.model.entity.QPetSitter.petSitter;

@Slf4j
public class PetSitterRepositoryImpl extends QuerydslRepositorySupport implements
        PetSitterRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public PetSitterRepositoryImpl() {
        super(PetSitter.class);
    }

    @Override
    public Page<PetSitter> searchPage(Pageable pageable, String dogSize, List<String> service,
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

    private BooleanExpression containsDogSize(QPetSitterCriticalService petSitterCriticalService, QPetSitter petSitter, String dogSize) {
        if (dogSize == null || dogSize.isEmpty()) {
            return null;
        }
        List<Long> petSitterIdList = queryFactory.select(petSitterCriticalService.petSitter.id)
                .from(petSitterCriticalService)
                .where(petSitterCriticalService.criticalService.serviceName.eq(dogSize)).fetch();
        return petSitter.id.in(petSitterIdList);
    }

    private BooleanExpression containService(QPetSitterWithPetService petSitterWithPetService, QPetSitter petSitter, List<String> service) {
        if (service == null || service.isEmpty()) {
            return null;
        }
        List<Long> petSitterIdList = queryFactory.select(petSitterWithPetService.petSitter.id)
                .from(petSitterWithPetService)
                .where(petSitterWithPetService.withPetService.serviceImg.in(service)).fetch();
        return petSitter.id.in(petSitterIdList);
    }

    private BooleanExpression containAddress(String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }
        List<Long> petSitterIdList = queryFactory.select(petSitter.id).from(petSitter)
                .where(petSitter.petSitterStreetAdr.contains(address)).fetch();
        return petSitter.petSitterStreetAdr.contains(address);
    }

}
