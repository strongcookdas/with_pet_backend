package com.ajou_nice.with_pet.diary.repository;

import com.ajou_nice.with_pet.diary.model.entity.Diary;
import java.time.LocalDate;
import java.util.List;

public interface DiaryRepositoryCustom {

    List<Diary> findByMonthDate(Long userId, Long dogId, Long categoryId, LocalDate month, String petsitterCheck);

    List<Diary> findByDayDate(Long userId, Long dogId, Long categoryId, LocalDate parse, String petsitterCheck);
    Long countDiaryDay(Long dogId,LocalDate createdAt);
    Long countDiary(Long dogId, LocalDate createdAt);
}
