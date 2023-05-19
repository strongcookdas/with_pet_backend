package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Diary;
import com.ajou_nice.with_pet.repository.custom.userdiary.DiaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long>,
        DiaryRepositoryCustom {

}
