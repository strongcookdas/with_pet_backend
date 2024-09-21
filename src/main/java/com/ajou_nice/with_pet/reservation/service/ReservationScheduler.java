package com.ajou_nice.with_pet.reservation.service;

import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;

import java.util.List;
import java.util.Optional;

import com.ajou_nice.with_pet.pay.service.KaKaoPayService;
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
	private final KaKaoPayService kaKaoPayService;

	// 매일 30분마다 reservation status가 WAIT이지만 결제가 안 이뤄졌을 경우나 혹은
	// 예약 checkIn이 내일인데 아직 결제하지 않는 경우에 자동 취소
	// 즉, 결제에 실패하고 나서 예약이 잡혀있는 건을 , 하루 내에 결제하지 않으면 AUTO_CANCEL로 변경시킨다.
	// 예약의 상태를 AUTO CANCEL로 업데이트시킨다.
	@Scheduled(fixedRate = 1200000)
	@Async
	public void scheduleReservation(){

		reservationRepository.executeAutoCancel(ReservationStatus.AUTO_CANCEL, ReservationStatus.WAIT);
	}


	//매일 새벽 3시에 reservation status가 USE일때, 이용완료를 2일이내로 변경해줌
	//이용완료된 예약 내역을 위해서 reservation status를 DONE으로 변경시켜준다.
	@Scheduled(fixedRate = 1800000)
	@Async
	public void scheduleDoneReservation(){
		reservationRepository.executeAutoDone(ReservationStatus.DONE, ReservationStatus.USE);
	}


	// 매일 새벽 3시에 reservation status가 Payed이며 checkin까지 3일이내로 남았을때
	// reservation status를 자동으로 AUTO_CANCEL로 변경해줌 + 자동 환불
	// 결제까지 마쳤으나, 펫시터가 승인하지 않는 경우
	// 자동 환불의 경우 kakaopay와 연동되어 환불이 실제로 일어나야 하므로 bulk 연산 x
	// 하나씩 해야한다.
	@Scheduled(fixedRate = 600000)
	@Async
	public void autoRefund(){

		Optional<List<Reservation>> reservations = reservationRepository.findNeedRefundReservation(ReservationStatus.PAYED);

		if(!reservations.isEmpty()){
			for(Reservation reservation : reservations.get()){
				kaKaoPayService.autoRefund(reservation);
			}
		}
	}

	//매일 새벽 00시 reservation Status가 Approval인 reservation에 대해 checkIn 날짜가 오늘과 같은
	//reservation status -> USE 로 변경
	@Scheduled(fixedRate = 600000)
	@Async
	public void autoUse(){

		reservationRepository.executeAutoUse(ReservationStatus.USE, ReservationStatus.APPROVAL);
	}
}