package com.ajou_nice.with_pet.diary.repository;

import static com.ajou_nice.with_pet.diary.model.entity.QDiary.diary;
import static com.ajou_nice.with_pet.domain.entity.QUserParty.userParty;

import com.ajou_nice.with_pet.diary.model.entity.Diary;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
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

        return queryFactory.select(diary).from(diary, userParty)
                .where(diary.dog.party.eq(userParty.party).and(userParty.user.id.eq(userId)
                                .and(diary.createdAt.between(month.withDayOfMonth(1),
                                        month.withDayOfMonth(month.lengthOfMonth())))),
                        containsDog(dogId), containsCategory(categoryId),
                        containsPetSitter(petsitterCheck))
                .orderBy(diary.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Diary> findByDayDate(Long userId, Long dogId, Long categoryId,
                                     LocalDate day, String petSitterCheck) {
        return queryFactory.select(diary).from(diary, userParty)
                .where(diary.dog.party.eq(userParty.party).and(userParty.user.id.eq(userId)
                                .and(diary.createdAt.eq(day))),
                        containsDog(dogId), containsCategory(categoryId),
                        containsPetSitter(petSitterCheck))
                .orderBy(diary.createdAt.desc())
                .fetch();
    }

    @Override
    public Long countDiaryDay(Long dogId, LocalDate createdAt) {
        return queryFactory.select(diary.dog.dogId.countDistinct()).from(diary)
                .where(diary.createdAt.between(createdAt, LocalDate.now()).and(diary.dog.dogId.eq(dogId))
                        .and(diary.petSitter.isNull())).fetchOne();
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
        return diary.diaryCategory.categoryId.eq(categoryId);
    }

    private BooleanExpression containsPetSitter(String petSitterCheck) {
        if (petSitterCheck == null || petSitterCheck.isEmpty()) {
            return null;
        }
        if (petSitterCheck.equals("PETSITTER")) {
            return diary.petSitter.isNotNull();
        } else if (petSitterCheck.equals("USER")) {
            return diary.petSitter.isNull();
        }

        return null;
    }

}
