package com.ajou_nice.with_pet.schedulers;

import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.service.KaKaoPayService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

	static{
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@DisplayName("예약 자동 취소 test")
	@Transactional
	@Test
	public void autoCanelTest() throws Exception{
	    // given
		// 2023-06-09 checkIn - 06-10 checkout 설정 (WAIT) -> 결제 전 AUTOCANCEL이 먹히는지 확인
		// AUTOCANCEL의 경우 예약이 결제전일때 예약이 생성된 날짜로부터 1일이내에 결제 하지 않았거나, checkIn이 현재날짜 기준 하루전일경우 AUTOCANCEL됨
		LocalDateTime checkIn = LocalDateTime.of(2023, 6, 9, 5, 13);
		LocalDateTime checkOut = LocalDateTime.of(2023, 6, 10, 6,13);
		Reservation reservation = Reservation.forSchedulerTest(checkIn, checkOut);
		reservationRepository.save(reservation);

		//when executeAutoCancel을 실행했을때
		reservationRepository.executeAutoCancel(ReservationStatus.AUTO_CANCEL, ReservationStatus.WAIT);
	  
	    //then 예약상태가 AUTO_CANCEL로 바뀌는지 확인
		Reservation reservation1 = reservationRepository.findById(reservation.getReservationId()).orElseThrow(()->{
			throw new AppException(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		});

		Assertions.assertEquals(ReservationStatus.AUTO_CANCEL, reservation1.getReservationStatus());
	}

	@DisplayName("예약 자동 완료 test")
	@Transactional
	@Test
	public void autoDoneTest() throws Exception{
		 //given
		 // AutoDone의 경우 사용자가 이용완료를 2일이내로 누르지 않은 USE 상태인 예약에 대해서 DONE으로 자동 변경 시켜준다.
		 LocalDateTime checkIn = LocalDateTime.of(2023, 6, 3, 5, 13);
		 LocalDateTime checkOut = LocalDateTime.of(2023, 6, 5, 1,13);
		 Reservation reservation = Reservation.forSchedulerTest(checkIn, checkOut);
		 reservationRepository.save(reservation);
	     //when AutoDone을 실행했을때
		 reservation.updateStatus(ReservationStatus.USE.toString());
		 reservationRepository.executeAutoDone(ReservationStatus.DONE, ReservationStatus.USE);
	   
	     //then DONE으로 예약상태가 바뀌는지 확인
		 Reservation reservation1 = reservationRepository.findById(reservation.getReservationId()).orElseThrow(()->{
			 throw new AppException(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		 });

		 Assertions.assertEquals(ReservationStatus.DONE, reservation1.getReservationStatus());
	     
	}
	@DisplayName("예약 자동 USE test")
	@Transactional
	@Test
	public void autoUseTest() throws Exception{
	    //given checkIn이 오늘이어야 함
		// autoUSE의 경우 매일 00시에 APPROVAL인 예약에 대해 checkIn날짜가 오늘이면 USE로 변경시켜준다.
		LocalDateTime checkIn = LocalDateTime.of(2023, 6, 12, 5, 13);
		LocalDateTime checkOut = LocalDateTime.of(2023, 6, 13, 1,13);
		Reservation reservation = Reservation.forSchedulerTest(checkIn, checkOut);
		reservationRepository.save(reservation);
	    //when autoUSE를 실행시켰을때
		reservation.updateStatus(ReservationStatus.APPROVAL.toString());
		reservationRepository.executeAutoUse(ReservationStatus.USE, ReservationStatus.APPROVAL);
	    //then 예약 상태가 USE로 변경되었는지 확인
		Reservation reservation1 = reservationRepository.findById(reservation.getReservationId()).orElseThrow(()->{
			throw new AppException(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		});
		Assertions.assertEquals(ReservationStatus.USE, reservation1.getReservationStatus());

	}
	  
}
