package com.ajou_nice.with_pet.repository.custom.userdiary;

import com.ajou_nice.with_pet.domain.entity.UserDiary;
import java.time.LocalDate;
import java.util.List;

public interface UserDiaryRepositoryCustom {

    List<UserDiary> findByMonthDate(Long userId, Long dogId, Long categoryId, LocalDate month);

    List<UserDiary> findByDayDate(Long userId, Long dogId, Long categoryId, LocalDate parse);
}
