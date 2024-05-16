package com.ajou_nice.with_pet.house.repository;

import com.ajou_nice.with_pet.house.model.entity.House;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface HouseRepository extends JpaRepository<House, Long> {

	@Query("select h from House h where h.petSitter.id=:petSitterId")
	List<House> findAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);

	@Modifying
	@Transactional
	@Query("delete from House h where h.petSitter.id=:petSitterId")
	void deleteAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);
}
