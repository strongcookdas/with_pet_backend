package com.ajou_nice.with_pet.diary.repository;

import com.ajou_nice.with_pet.diary.model.entity.Diary;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {

    @Query("select distinct d from Diary d join fetch d.petSitter join fetch d.dog where d.petSitter=:petSitter and d.dog=:dog order by d.diaryId desc ")
    List<Diary> findAllByPetSitterAndDog(@Param("petSitter") PetSitter petSitter,
            @Param("dog") Dog dog);
}
