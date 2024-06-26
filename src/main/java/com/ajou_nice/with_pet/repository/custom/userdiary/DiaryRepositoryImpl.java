package com.ajou_nice.with_pet.repository.custom.userdiary;

import static com.ajou_nice.with_pet.domain.entity.QDiary.diary;
import static com.ajou_nice.with_pet.domain.entity.QUserParty.userParty;

import com.ajou_nice.with_pet.domain.entity.Diary;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


public class DiaryRepositoryImpl extends QuerydslRepositorySupport implements
        DiaryRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public DiaryRepositoryImpl() {
        super(Diary.class);
    }

    @Override
    public List<Diary> findByMonthDate(Long userId, Long dogId, Long categoryId,
            LocalDate month, String petsitterCheck) {

        List<Diary> userDiaries = queryFactory.select(diary).from(diary, userParty)
                .where(diary.dog.party.eq(userParty.party).and(userParty.user.id.eq(userId)
                                .and(diary.createdAt.between(month.withDayOfMonth(1),
                                        month.withDayOfMonth(month.lengthOfMonth())))),
                        containsDog(dogId), containsCategory(categoryId),
                        containsPetsitter(petsitterCheck))
                .orderBy(diary.createdAt.desc())
                .fetch();
        return userDiaries;
    }

    @Override
    public List<Diary> findByDayDate(Long userId, Long dogId, Long categoryId,
            LocalDate day, String petsitterCheck) {
        List<Diary> userDiaries = queryFactory.select(diary).from(diary, userParty)
                .where(diary.dog.party.eq(userParty.party).and(userParty.user.id.eq(userId)
                                .and(diary.createdAt.eq(day))),
                        containsDog(dogId), containsCategory(categoryId),
                        containsPetsitter(petsitterCheck))
                .orderBy(diary.createdAt.desc())
                .fetch();
        return userDiaries;
    }

    @Override
    public Long countDiaryDay(Long dogId, LocalDate createdAt) {
        return queryFactory.select(diary.dog.dogId.countDistinct()).from(diary)
                .where(diary.createdAt.between(createdAt, LocalDate.now()).and(diary.dog.dogId.eq(dogId)).and(diary.petSitter.isNull())).fetchOne();
    }

    @Override
    public Long countDiary(Long dogId, LocalDate createdAt) {
        return queryFactory.select(diary.dog.dogId.count()).from(diary)
                .where(diary.createdAt.between(createdAt, LocalDate.now()).and(diary.dog.dogId.eq(dogId))).fetchOne();
    }

    private BooleanExpression containsDog(Long dogId) {
        if (dogId == null) {
            return null;
        }
        return diary.dog.dogId.eq(dogId);
    }

    private BooleanExpression containsCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return diary.category.categoryId.eq(categoryId);
    }

    private BooleanExpression containsPetsitter(String petsitterCheck) {
        if (petsitterCheck == null || petsitterCheck.isEmpty()) {
            return null;
        }
        if (petsitterCheck.equals("PETSITTER")) {
            return diary.petSitter.isNotNull();
        } else if (petsitterCheck.equals("USER")) {
            return diary.petSitter.isNull();
        }

        return null;
    }

}
