package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
