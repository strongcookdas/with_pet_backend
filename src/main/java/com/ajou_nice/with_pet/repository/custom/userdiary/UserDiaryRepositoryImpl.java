package com.ajou_nice.with_pet.repository.custom.userdiary;

import static com.ajou_nice.with_pet.domain.entity.QUserDiary.userDiary;
import static com.ajou_nice.with_pet.domain.entity.QUserParty.userParty;

import com.ajou_nice.with_pet.domain.entity.UserDiary;
import com.ajou_nice.with_pet.enums.Category;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


public class UserDiaryRepositoryImpl extends QuerydslRepositorySupport implements
        UserDiaryRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public UserDiaryRepositoryImpl() {
        super(UserDiary.class);
    }

    @Override
    public List<UserDiary> findBySearchOption(Long userId, Long dogId, Category category,
            LocalDate month) {

        List<UserDiary> userDiaries = queryFactory.select(userDiary).from(userDiary, userParty)
                .where(userDiary.dog.party.eq(userParty.party).and(userParty.user.userId.eq(userId)
                                .and(userDiary.createdAt.between(month.withDayOfMonth(1),month.withDayOfMonth(month.lengthOfMonth())))),
                        containsDog(dogId), containsCategory(category))
                .fetch();
        return userDiaries;
    }

    private BooleanExpression containsDog(Long dogId) {
        if (dogId == null) {
            return null;
        }
        return userDiary.dog.dogId.eq(dogId);
    }

    private BooleanExpression containsCategory(Category category) {
        if (category == null) {
            return null;
        }
        return userDiary.category.eq(category);
    }
}
