package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(String userId);
    Optional<User> findByUserId(Long Id);
    boolean existsById(String userId);

    @Query("select a from User a where a.role=:userRole and a.applicantStatus=:applicantStatus")
    List<User> findApplicantAllInQuery(@Param("userRole") UserRole userRole, @Param("applicantStatus")ApplicantStatus applicantStatus);
}
