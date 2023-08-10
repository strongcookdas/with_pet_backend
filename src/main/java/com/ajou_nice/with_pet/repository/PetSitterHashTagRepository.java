package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.HashTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PetSitterHashTagRepository extends JpaRepository<HashTag, Long> {

	@Query("select t from HashTag t where t.petSitter.petSitterId=:petSitterId")
	List<HashTag> findAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);

	@Modifying
	@Transactional
	@Query("delete from HashTag t where t.petSitter.petSitterId=:petSitterId")
	void deleteAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);
}
