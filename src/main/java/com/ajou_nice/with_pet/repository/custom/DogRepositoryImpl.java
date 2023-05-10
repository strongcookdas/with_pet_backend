package com.ajou_nice.with_pet.repository.custom;

import static com.ajou_nice.with_pet.domain.entity.QDog.dog;
import static com.ajou_nice.with_pet.domain.entity.QUserParty.userParty;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class DogRepositoryImpl extends QuerydslRepositorySupport implements DogRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public DogRepositoryImpl() {
        super(Dog.class);
    }

    @Override
    public Page<Dog> findAllByUserParty(Pageable pageable, String userId) {
        List<Dog> dogs = queryFactory.select(dog)
                .from(dog, userParty)
                .where(dog.party.eq(userParty.party).and(userParty.user.id.eq(userId))).fetch();
        log.info(
                "--------------------------------Query DSL 디버깅----------------------------------------");
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dogs.size());
        return new PageImpl<Dog>(dogs.subList(start, end), pageable, dogs.size());
    }
}
