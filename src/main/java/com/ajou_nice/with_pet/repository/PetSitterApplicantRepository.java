package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import java.util.List;
import java.util.Optional;
import javax.persistence.EnumType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetSitterApplicantRepository extends JpaRepository<PetSitterApplicant, Long> {
	Optional<PetSitterApplicant> findById(Long id);

	Optional<PetSitterApplicant> findByUser(User user);


	@Query("select a from PetSitterApplicant a where a.applicantStatus=:applicantStatus")
	List<PetSitterApplicant> findAllInQuery(@Param("applicantStatus") ApplicantStatus applicantStatus);

}
