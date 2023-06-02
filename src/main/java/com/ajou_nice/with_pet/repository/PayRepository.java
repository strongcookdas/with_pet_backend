package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Pay;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PayRepository extends JpaRepository<Pay,Long> {

	@Query("select p from Pay p where p.reservation=:reservation")
	Optional<Pay> findByReservation(@Param("reservation") Reservation reservation);

	@Query("select p from Pay p where p.tid=:tid")
	Optional<Pay> findByTid(@Param("tid") String tid);
}
