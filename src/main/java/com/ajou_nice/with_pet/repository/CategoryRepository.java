package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
