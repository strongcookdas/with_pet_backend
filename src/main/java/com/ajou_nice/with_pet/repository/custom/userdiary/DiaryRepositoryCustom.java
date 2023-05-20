package com.ajou_nice.with_pet.repository.custom.userdiary;

import com.ajou_nice.with_pet.domain.entity.Diary;
import java.time.LocalDate;
import java.util.List;

public interface DiaryRepositoryCustom {

    List<Diary> findByMonthDate(Long userId, Long dogId, Long categoryId, LocalDate month, String petsitterCheck);

    List<Diary> findByDayDate(Long userId, Long dogId, Long categoryId, LocalDate parse, String petsitterCheck);
}
