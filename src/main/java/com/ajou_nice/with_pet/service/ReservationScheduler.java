package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.entity.Pay;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PayRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional
public class ReservationScheduler {

	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService;

	//매일 새벽 3시에 reservation status가 WAIT이지만 예약이 7일 이내로 남았을 경우
	//즉, 결제에 실패하고 나서 checkin이 7일이내로 남았지만, 아직 결제가 안된 경우
	//예약의 상태를 AUTO CANCEL로 업데이트시킨다.
	@Scheduled(fixedRate = 10000)
	@Async
	public void scheduleReservation(){

		reservationRepository.executeAutoCancel(ReservationStatus.AUTO_CANCEL, ReservationStatus.WAIT);
	}


	//매일 새벽 3시에 reservation status가 Approval일때, 이용완료를 3일이내로 변경해줌
	//이용완료된 예약 내역을 위해서 reservation status를 DONE으로 변경시켜준다.
	@Scheduled(fixedRate = 10000)
	@Async
	public void scheduleDoneReservation(){
		reservationRepository.executeAutoDone(ReservationStatus.DONE, ReservationStatus.APPROVAL);
	}


	// 매일 새벽 3시에 reservation status가 Payed이며 checkin까지 3일이내로 남았을때
	// reservation status를 자동으로 AUTO_CANCEL로 변경해줌 + 자동 환불
	// 자동 환불의 경우 kakaopay와 연동되어 환불이 실제로 일어나야 하므로 bulk 연산 x
	// 하나씩 해야한다.
	/*
	@Scheduled(fixedRate = 10000)
	@Async
	public void autoRefund(){
		List<Reservation> reservations = reservationRepository.find
	}

	 */
}