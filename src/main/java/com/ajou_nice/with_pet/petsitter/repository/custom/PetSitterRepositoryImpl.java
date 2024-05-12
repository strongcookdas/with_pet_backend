package com.ajou_nice.with_pet.petsitter.repository.custom;

import static com.ajou_nice.with_pet.domain.entity.QPetSitter.petSitter;

import com.ajou_nice.with_pet.domain.entity.QPetSitter;
import com.ajou_nice.with_pet.domain.entity.QPetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.QPetSitterWithPetService;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
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
    public Page<PetSitter> searchPage(Pageable pageable, String dogSize, List<String> service,
            String address) {
        log.info("======================= 메인필터링 시작 ========================");

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
        log.info("======================= 메인필터링 끝 ========================");
        return new PageImpl<PetSitter>(petSitters.subList(start, end), pageable, petSitters.size());
    }

    private BooleanExpression containsDogSize(QPetSitterCriticalService petSitterCriticalService,
            QPetSitter petSitter,
            String dogSize) {
        log.info("======================= 필수 서비스 필터 시작 ========================");
        if (dogSize == null || dogSize.isEmpty()) {
            return null;
        }
        List<Long> petSitterIdList = queryFactory.select(petSitterCriticalService.petSitter.id)
                .from(petSitterCriticalService)
                .where(petSitterCriticalService.criticalService.serviceName.eq(dogSize)).fetch();
        log.info("======================= 필수 서비스 필터 끝 ========================");
        return petSitter.id.in(petSitterIdList);
    }

    private BooleanExpression containService(QPetSitterWithPetService petSitterWithPetService,
            QPetSitter petSitter,
            List<String> service) {
        log.info("======================= 서비스 필터 시작 ========================");
        if (service == null || service.isEmpty()) {
            return null;
        }
        List<Long> petSitterIdList = queryFactory.select(petSitterWithPetService.petSitter.id)
                .from(petSitterWithPetService)
                .where(petSitterWithPetService.withPetService.serviceImg.in(service)).fetch();
        log.info("======================= 서비스 필터 끝 ========================");

        return petSitter.id.in(petSitterIdList);
    }

    private BooleanExpression containAddress(String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }
        log.info("======================= 주소 필터 시작 ========================");
        List<Long> petSitterIdList = queryFactory.select(petSitter.id).from(petSitter)
                .where(petSitter.petSitterStreetAdr.contains(address)).fetch();
        log.info("======================= 주소 필터 끝 ========================");
        return petSitter.petSitterStreetAdr.contains(address);
    }

}
