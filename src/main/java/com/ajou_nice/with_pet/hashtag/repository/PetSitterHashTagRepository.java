package com.ajou_nice.with_pet.hashtag.repository;

import com.ajou_nice.with_pet.hashtag.model.entity.PetSitterHashTag;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PetSitterHashTagRepository extends JpaRepository<PetSitterHashTag, Long> {

	@Query("select t from PetSitterHashTag t where t.petSitter.id=:petSitterId")
	List<PetSitterHashTag> findAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);

	@Modifying
	@Transactional
	@Query("delete from PetSitterHashTag t where t.petSitter.id=:petSitterId")
	void deleteAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);
}
