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
import com.ajou_nice.with_pet.service.ReservationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ReservationListTest {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PetSitterRepository petSitterRepository;
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ReservationController reservationController;
	@Autowired
	ReservationService reservationService;

	static{
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	private User user;
	private PetSitter petSitter;

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


	@DisplayName("반려인의 예약 list test code")
	@Transactional
	@Test
	public void reservationListTest() throws Exception{
	    //given
	    initialize();
		LocalDateTime checkIn = LocalDateTime.of(2023, 6, 13, 5, 13);
		LocalDateTime checkOut = LocalDateTime.of(2023, 6, 14, 6, 13);
		Reservation reservation1 = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 35000);
		reservation1.updateStatus(ReservationStatus.APPROVAL.toString());
		reservationRepository.save(reservation1);

		checkIn = LocalDateTime.of(2023, 6, 14, 5, 13);
		checkOut = LocalDateTime.of(2023, 6, 15, 6, 13);
		Reservation reservation2 = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 40000);
		reservation2.updateStatus(ReservationStatus.WAIT.toString());
		reservationRepository.save(reservation2);

		checkIn = LocalDateTime.of(2023, 6, 15, 5, 13);
		checkOut = LocalDateTime.of(2023, 6, 16, 6, 13);
		Reservation reservation3 = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 39000);
		reservation3.updateStatus(ReservationStatus.PAYED.toString());
		reservationRepository.save(reservation3);

		checkIn = LocalDateTime.of(2023, 6, 7, 5, 13);
		checkOut = LocalDateTime.of(2023, 6, 8, 6, 13);
		Reservation reservation4 = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 47000);
		reservation4.updateStatus(ReservationStatus.APPROVAL.toString());
		reservationRepository.save(reservation4);

	    //when
		Optional<List<Reservation>> myReservations = reservationRepository.findAllByUser(user);

	    //then
		System.out.println(myReservations.get().toString());
	    
	 }
	  
}
