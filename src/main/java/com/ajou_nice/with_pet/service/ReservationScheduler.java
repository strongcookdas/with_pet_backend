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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ReservationScheduler {

	private final ReservationRepository reservationRepository;
	private final KaKaoPayService kaKaoPayService;

	//매일 오후 12시에 reservation status가 PAYED이지만 예약이 7일 이내로 남았을 경우
	//즉 결제는 했지만, 펫시터가 승락을 안해줘 예약이 안되는 경우
	//예약의 상태를 cancel로 자동 취소 시키고 , 자동 100% 환불 시켜준다.
	@Scheduled(cron = "0 0 12 * * *")
	public void scheduleReservation(){

		LocalDateTime nowPlusSevenDays = LocalDateTime.now().plusDays(7);
		List<Reservation> needCanceledReservations = reservationRepository.findReservationByDateAndStatus(nowPlusSevenDays,
				ReservationStatus.PAYED.toString());

		if(!needCanceledReservations.isEmpty()){
			for(Reservation reservation : needCanceledReservations){
				kaKaoPayService.refundAuto(reservation);
			}
		}
	}
}
