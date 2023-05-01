package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(String userId);
}
