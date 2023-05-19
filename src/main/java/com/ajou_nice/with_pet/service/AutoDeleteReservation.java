package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.repository.ReservationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoDeleteReservation {

	private final ReservationRepository reservationRepository;

	//매일 예약 시작까지 삼일의 시간이 남았고, reservation status가 승인인 reservation에 대해 삭제
	//매일 4시간 마다 실행
	//cron = "0 0 0/4 * * *"
	@Scheduled(fixedDelay = 100000)
	public void deleteReservation(){

		LocalDateTime now = LocalDateTime.now();
		//현재로부터 삼일 뒤의 localDate time
		LocalDateTime needDeleteTime = now.plusDays(3);

		reservationRepository.deleteReservationByCheckInTime(needDeleteTime);

	}
}
