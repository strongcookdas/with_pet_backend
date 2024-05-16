package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {


	@Query("select r from Review r where r.petSitter = :petSitter")
	List<Review> findAllByPetSitter(@Param("petSitter") PetSitter petSitter);
}
