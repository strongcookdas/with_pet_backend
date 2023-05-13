package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.House;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HouseRepository extends JpaRepository<House, Long> {

	@Query("select h from House h where h.petSitter.id=:petSitterId")
	Optional<List<House>> findAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);

	@Query("delete from House h where h.petSitter.id=:petSitterId")
	void deleteAllByPetSitterInQuery(@Param("petSitterId") Long petSitterId);
}
