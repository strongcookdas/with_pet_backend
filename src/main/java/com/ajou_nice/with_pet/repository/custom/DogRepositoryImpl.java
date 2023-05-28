package com.ajou_nice.with_pet.repository.custom;


import static com.ajou_nice.with_pet.domain.entity.QDog.dog;
import static com.ajou_nice.with_pet.domain.entity.QUserParty.userParty;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class DogRepositoryImpl extends QuerydslRepositorySupport implements DogRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public DogRepositoryImpl() {
        super(Dog.class);
    }

    @Override
    public List<Dog> findAllByUserParty(String userId) {
        List<Dog> dogs = queryFactory.select(dog)
                .from(dog, userParty)
                .where(dog.party.eq(userParty.party).and(userParty.user.id.eq(userId))).fetch();
        log.info(
                "--------------------------------Query DSL 디버깅----------------------------------------");
        return dogs;
    }
}
