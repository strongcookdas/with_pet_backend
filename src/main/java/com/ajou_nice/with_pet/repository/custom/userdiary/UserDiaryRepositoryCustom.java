package com.ajou_nice.with_pet.repository.custom.userdiary;

import com.ajou_nice.with_pet.domain.entity.UserDiary;
import com.ajou_nice.with_pet.enums.Category_1;
import java.time.LocalDate;
import java.util.List;

public interface UserDiaryRepositoryCustom {

    List<UserDiary> findByMonthDate(Long userId, Long dogId, Category_1 category1, LocalDate month);

    List<UserDiary> findByDayDate(Long userId, Long dogId, Category_1 category1, LocalDate parse);
}
