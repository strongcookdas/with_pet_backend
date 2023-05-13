package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.UserDiary;
import com.ajou_nice.with_pet.repository.custom.userdiary.UserDiaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long>,
        UserDiaryRepositoryCustom {

}
