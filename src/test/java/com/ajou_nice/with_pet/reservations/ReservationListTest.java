package com.ajou_nice.with_pet.reservations;


import com.ajou_nice.with_pet.controller.ReservationController;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationDocsResponse;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.fixture.Fixture;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ReservationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

	@Autowired
	DogRepository dogRepository;

	@Autowired
	PartyRepository partyRepository;

	Fixture fixture = new Fixture();

	static{
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	private User user;
	private PetSitter petSitter;

	private Dog dog;
	private Party party;

	@Transactional
	public void initialize(){
		Address address = Address.simpleAddressGenerator("213","adasd", "244");
		user = fixture.getUser1();
		User user2 = User.simpleUserForTest("장승현4", "simplepetsitter4", "1234", "jason5102@ajou.ac.kr",
				UserRole.ROLE_PETSITTER, "010-3931-5102", address);
		userRepository.save(user); userRepository.save(user2);

		petSitter = PetSitter.simplePetSitterForTest(user2.getName(), user2.getPhone(), "www.google.com", "213", "sdfs",
				"dsfds", user2);

		party = fixture.getParty();
		dog = fixture.getDog();
		partyRepository.save(party);
		dogRepository.save(dog);
		petSitterRepository.save(petSitter);
	}


	@DisplayName("반려인의 예약 list test code")
	@Transactional
	@Test
	public void reservationListTest() throws Exception{
	    //given 4개의 예약이 주어졌을때 (승인된 예약2개 , 결제된 예약 1개, 결제대기중 예약 1개)
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

	    //when (user가 자신의 예약 찾았을때)
		Optional<List<Reservation>> myReservations = reservationRepository.findAllByUser(user);
		List<Reservation> waitReservations = myReservations.get().stream().filter(
				reservation -> reservation.getReservationStatus().equals(ReservationStatus.WAIT)).collect(
				Collectors.toList());
		List<Reservation> doneReservations = myReservations.get().stream().filter(
				reservation -> reservation.getReservationStatus().equals(ReservationStatus.DONE)).collect(
				Collectors.toList());

	    //then (myReservations로 waitReservations와 doneReservations로 나누었을때 잘 되는지 확인)
		Assertions.assertEquals(waitReservations.get(0),reservation2);
		Assertions.assertTrue(doneReservations.isEmpty());

	}

	@DisplayName("반려인의 예약 list test code")
	@Transactional
	@Test
	public void getReservationDocsTest() throws Exception{

		//given 4개의 예약이 주어졌을때 (승인된 예약2개 , 결제된 예약 1개, 결제대기중 예약 1개)
		initialize();
		LocalDateTime checkIn = LocalDateTime.of(2023, 6, 13, 5, 13);
		LocalDateTime checkOut = LocalDateTime.of(2023, 6, 14, 6, 13);
		Reservation reservation1 = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 35000,
				ReservationStatus.APPROVAL, dog, "소형견", 10000);
		reservationRepository.save(reservation1);

		checkIn = LocalDateTime.of(2023, 6, 14, 5, 13);
		checkOut = LocalDateTime.of(2023, 6, 15, 6, 13);
		Reservation reservation2 = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 40000,
				ReservationStatus.WAIT, dog, "중형견", 20000);
		reservationRepository.save(reservation2);

		checkIn = LocalDateTime.of(2023, 6, 15, 5, 13);
		checkOut = LocalDateTime.of(2023, 6, 16, 6, 13);
		Reservation reservation3 = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 39000,
				ReservationStatus.PAYED, dog, "대형견", 35000);
		reservationRepository.save(reservation3);

		checkIn = LocalDateTime.of(2023, 6, 7, 5, 13);
		checkOut = LocalDateTime.of(2023, 6, 8, 6, 13);
		Reservation reservation4 = Reservation.forSimpleTest(checkIn, checkOut, user, petSitter, 47000,
				ReservationStatus.APPROVAL, dog, "소형견", 15000);
		reservationRepository.save(reservation4);
	   
		//when ReservationDocsResponse에 매핑했을때
		ReservationDocsResponse docsResponse = reservationService.getReservationDoc(user.getId());

		//then Dto에 생각한대로 잘 매핑되는지 출력
		System.out.println(docsResponse);
	     
	}
	   
	  
}
