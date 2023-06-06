package com.ajou_nice.with_pet.schedulers;

import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.service.KaKaoPayService;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class SchedulerTest {

	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	KaKaoPayService kaKaoPayService;

	@Transactional
	@Test
	public void autoCanelTest() throws Exception{
	    //given
		// 2023-06-05 checkIn - 06-06 checkout 설정 (WAIT) -> 결제 전 AUTOCANCEL이 먹히는지 확인
		LocalDateTime checkIn = LocalDateTime.of(2023, 6, 5, 5, 13);
		LocalDateTime checkOut = LocalDateTime.of(2023, 6, 6, 6,13);
		Reservation reservation = Reservation.forSchedulerTest(checkIn, checkOut);
		reservationRepository.save(reservation);
	    //when
		reservationRepository.executeAutoCancel(ReservationStatus.AUTO_CANCEL, ReservationStatus.WAIT);
	  
	    //then
		Reservation reservation1 = reservationRepository.findById(reservation.getReservationId()).orElseThrow(()->{
			throw new AppException(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		});

		Assertions.assertEquals(ReservationStatus.AUTO_CANCEL, reservation1.getReservationStatus());
	 }

	 @Transactional
	 @Test
	 public void autoDoneTest() throws Exception{
		 //given
		 // 2023-06-05 checkIn - 06-06 checkout 설정 (WAIT) -> 결제 전 AUTOCANCEL이 먹히는지 확인
		 LocalDateTime checkIn = LocalDateTime.of(2023, 6, 3, 5, 13);
		 LocalDateTime checkOut = LocalDateTime.of(2023, 6, 4, 1,13);
		 Reservation reservation = Reservation.forSchedulerTest(checkIn, checkOut);
		 reservationRepository.save(reservation);
	     //when
		 reservation.updateStatus(ReservationStatus.APPROVAL.toString());
		 reservationRepository.executeAutoDone(ReservationStatus.DONE, ReservationStatus.APPROVAL);
	   
	     //then
		 Reservation reservation1 = reservationRepository.findById(reservation.getReservationId()).orElseThrow(()->{
			 throw new AppException(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		 });

		 Assertions.assertEquals(ReservationStatus.DONE, reservation1.getReservationStatus());
	     
	  }
	   
	  
}
