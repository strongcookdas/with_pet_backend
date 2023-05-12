package com.ajou_nice.with_pet.repository.custom.userdiary;

import com.ajou_nice.with_pet.domain.entity.UserDiary;
import com.ajou_nice.with_pet.enums.Category;
import java.time.LocalDate;
import java.util.List;

public interface UserDiaryRepositoryCustom {

    List<UserDiary> findByMonthDate(Long userId, Long dogId, Category category, LocalDate month);

    List<UserDiary> findByDayDate(Long userId, Long dogId, Category category, LocalDate parse);
}
