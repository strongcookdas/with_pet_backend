package com.ajou_nice.with_pet.reservations;


import com.ajou_nice.with_pet.controller.ReservationController;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ReservationTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PetSitterRepository petSitterRepository;

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	ReservationController reservationController;


	private User user;
	private PetSitter petSitter;

	static{
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}


	@Transactional
	public void initialize(){
		Address address = Address.simpleAddressGenerator("213","adasd", "244");
		user = User.simpleUserForTest("장승현", "simpleuser", "1234", "jason5102@ajou.ac.kr",
				UserRole.ROLE_USER, "010-3931-5102", address);
		User user2 = User.simpleUserForTest("장승현2", "simplepetsitter", "1234", "jason5102@ajou.ac.kr",
				UserRole.ROLE_PETSITTER, "010-3931-5102", address);
		userRepository.save(user); userRepository.save(user2);

		petSitter = PetSitter.simplePetSitterForTest(user2.getName(), user2.getPhone(), "www.google.com", "213", "sdfs",
				"dsfds", user2);
		petSitterRepository.save(petSitter);
	}
	@DisplayName("사용자의 이용 완료 신청 test")
	@Transactional
	@Test
	public void reservationDoneTest() throws Exception{
	    //given
		initialize();
		LocalDateTime checkIn = LocalDateTime.of(2023, 6, 5, 5, 13);
		LocalDateTime checkOut = LocalDateTime.of(2023, 6, 6, 6, 13);
		Reservation reservation = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 30000);
		reservation.updateStatus(ReservationStatus.APPROVAL.toString());
		reservationRepository.save(reservation);
	    //when
		reservationController.doneReservation(reservation.getUser().getId(), reservation.getReservationId());
	    //then
		Assertions.assertEquals(reservation.getReservationStatus(), ReservationStatus.DONE);
	 }
 	@DisplayName("사용자의 결제 전 예약건에 대한 예약 취소")
 	@Transactional
 	@Test
 	public void reservationCacnelTest() throws Exception{
	     //given
		 initialize();
		 LocalDateTime checkIn = LocalDateTime.of(2023, 6, 5, 5, 13);
		 LocalDateTime checkOut = LocalDateTime.of(2023, 6, 6, 6, 13);
		 Reservation reservation = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 30000);
		 reservation.updateStatus(ReservationStatus.WAIT.toString());
		 reservationRepository.save(reservation);
	     //when
		 reservationController.cancelReservation(reservation.getUser().getId(), reservation.getReservationId());
	   
	     //then
		 Assertions.assertEquals(reservation.getReservationStatus(), ReservationStatus.CANCEL);
	}
}
