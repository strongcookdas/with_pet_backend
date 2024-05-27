package com.ajou_nice.with_pet.group.service;

import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.dog.repository.DogRepository;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.group.model.dto.PartyAddPartyByIsbnRequest;
import com.ajou_nice.with_pet.group.model.dto.PartyAddPartyByIsbnResponse;
import com.ajou_nice.with_pet.group.model.dto.add.PartyAddRequest;
import com.ajou_nice.with_pet.group.model.dto.add.PartyAddResponse;
import com.ajou_nice.with_pet.group.model.dto.get.PartyGetInfosResponse;
import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.group.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyService {
    private final Integer PARTY_MAX_COUNT = 5;
    private final Integer PARTY_MEMBER_MAX_COUNT = 5;

    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final DogRepository dogRepository;
    private final ReservationRepository reservationRepository;

    private final ValidateCollection valid;

    @Transactional
    public PartyAddPartyByIsbnResponse addPartyByPartyIsbn(String email, PartyAddPartyByIsbnRequest partyAddPartyByIsbnRequest) {
        User user = userValidationByEmail(email);
        Party party = partyValidationByIsbn(partyAddPartyByIsbnRequest.getPartyIsbn());

        checkPartyMemberAddValidation(user, party);

        return addPartyMember(user, party);
    }

    public List<PartyGetInfosResponse> getPartyInfoList(String email) {
        userValidationByEmail(email);
        return processUserPartyInfos(email);
    }

    @Transactional
    public PartyAddResponse createParty(String email, PartyAddRequest partyAddRequest) {
        // 파티 생성할 때 반려견을 등록해야 함
        User user = userValidationByEmail(email);
        Party party = userPartyCountCheckAndGenerationParty(user, partyAddRequest);
        Dog dog = registerPartyDog(party, partyAddRequest);

        return PartyAddResponse.of(dog);
    }

    @Transactional
    public String leaveParty(String userId, Long partyId) {

        User user = valid.userValidationById(userId);

        Party party = valid.partyValidation(partyId);

        List<Dog> dogs = dogRepository.findAllByParty(party);

        List<ReservationStatus> reservationStatuses = new ArrayList<>();
        reservationStatuses.add(ReservationStatus.APPROVAL);
        reservationStatuses.add(ReservationStatus.PAYED);
        reservationStatuses.add(ReservationStatus.USE);
        reservationStatuses.add(ReservationStatus.WAIT);

        if (reservationRepository.existsByUserAndDogInAndReservationStatusIn(user, dogs, reservationStatuses)) {
            throw new AppException(ErrorCode.CAN_NOT_LEAVE_PARTY, ErrorCode.CAN_NOT_LEAVE_PARTY.getMessage());
        }

        Optional<UserParty> deleteUserParty = userPartyRepository.findByUserAndParty(user, party);
        if (deleteUserParty.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_GROUP_MEMBER,
                    ErrorCode.NOT_FOUND_GROUP_MEMBER.getMessage());
        }

        Optional<UserParty> nextLeader = userPartyRepository.findFirstByUserNotAndParty(user,
                party);

        userPartyRepository.delete(deleteUserParty.get());
        user.updatePartyCount(user.getPartyCount() - 1);
        party.updateMemberCount(party.getMemberCount() - 1);

        if (party.getPartyLeader().getId().equals(user.getId())
                && !nextLeader.isEmpty()) {
            party.updatePartyLeader(
                    userPartyRepository.findFirstByUserNotAndParty(user, party).get().getUser());
            return nextLeader.get().getUser().getName() + "님이 방장이 되었습니다.";
        }

        return "그룹에서 탈퇴되었습니다.";
    }

    @Transactional
    public String expelMember(String userId, Long partyId, Long memberId) {

        User leader = valid.userValidationById(userId);

        User member = valid.userValidationById(memberId);

        Party party = valid.partyValidation(partyId);

        List<Dog> dogs = dogRepository.findAllByParty(party);

        List<ReservationStatus> reservationStatuses = new ArrayList<>();
        reservationStatuses.add(ReservationStatus.APPROVAL);
        reservationStatuses.add(ReservationStatus.PAYED);
        reservationStatuses.add(ReservationStatus.USE);
        reservationStatuses.add(ReservationStatus.WAIT);

        if (reservationRepository.existsByUserAndDogInAndReservationStatusIn(member, dogs, reservationStatuses)) {
            throw new AppException(ErrorCode.CAN_NOT_EXPEL_PARTY, ErrorCode.CAN_NOT_EXPEL_PARTY.getMessage());
        }

        if (!leader.getId().equals(party.getPartyLeader().getId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "멤버를 방출시킬 권한이 없습니다.");
        }

        Optional<UserParty> userParty = userPartyRepository.findByUserAndParty(member, party);
        if (userParty.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_GROUP_MEMBER,
                    ErrorCode.NOT_FOUND_GROUP_MEMBER.getMessage());
        }

        userPartyRepository.delete(userParty.get());
        member.updatePartyCount(member.getPartyCount() - 1);
        party.updateMemberCount(party.getMemberCount() - 1);

        return member.getName() + "님이 그룹에서 방출되었습니다.";
    }

    private User userValidationByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        return user;
    }

    private Party partyValidationByIsbn(String isbn) {
        Party party = partyRepository.findByPartyIsbn(isbn)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.GROUP_NOT_FOUND,
                            ErrorCode.GROUP_NOT_FOUND.getMessage());
                });
        return party;
    }

    private void checkUserPartyCount(User user) {
        if (user.getPartyCount() >= PARTY_MAX_COUNT) {
            throw new AppException(ErrorCode.TOO_MANY_GROUP, ErrorCode.TOO_MANY_GROUP.getMessage());
        }
    }

    private Party userPartyCountCheckAndGenerationParty(User partyLeader, PartyAddRequest partyAddRequest) {

        checkUserPartyCount(partyLeader);

        Party party = Party.of(partyLeader, partyAddRequest.getPartyName());
        party = partyRepository.save(party);

        UserParty userParty = UserParty.of(partyLeader, party);
        userPartyRepository.save(userParty);
        partyLeader.updatePartyCount(partyLeader.getPartyCount() + 1);

        return party;
    }

    private Dog registerPartyDog(Party party, PartyAddRequest partyAddRequest) {

        DogSize myDogSize;

        if (partyAddRequest.getPartyDogWeight() > 18) {
            myDogSize = DogSize.대형견;
        } else if (partyAddRequest.getPartyDogWeight() > 10) {
            myDogSize = DogSize.중형견;
        } else {
            myDogSize = DogSize.소형견;
        }

        Dog dog = Dog.of(partyAddRequest, party, myDogSize);
        dog = dogRepository.save(dog);

        return dog;
    }

    private List<PartyGetInfosResponse> processUserPartyInfos(String email) {
        List<Long> userPartyIdList = userPartyRepository.findAllUserPartyIdByUserId(email);
        List<Party> partyList = partyRepository.findAllByUserPartyId(userPartyIdList);
        List<Dog> dogList = dogRepository.findAllUserDogs(userPartyIdList);

        List<PartyGetInfosResponse> partyGetInfosResponses = partyList.stream()
                .map(PartyGetInfosResponse::of).collect(Collectors.toList());

        partyGetInfosResponses
                .forEach(partyGetInfosResponse ->
                        dogList.stream()
                                .filter(dog -> partyGetInfosResponse.getPartyId().equals(dog.getParty().getPartyId()))
                                .forEach(partyGetInfosResponse::toPartyGetInfosDogResponseAndAddDogList)
                );
        return partyGetInfosResponses;
    }

    private void checkPartyMemberAddValidation(User user, Party party) {
        checkUserPartyCount(user);

        if (party.getMemberCount() >= PARTY_MEMBER_MAX_COUNT) {
            throw new AppException(ErrorCode.TOO_MANY_MEMBER,
                    ErrorCode.TOO_MANY_MEMBER.getMessage());
        }
    }

    private PartyAddPartyByIsbnResponse addPartyMember(User user, Party party) {
        userPartyRepository.save(UserParty.of(user, party));
        user.updatePartyCount(user.getPartyCount() + 1);
        party.updateMemberCount(party.getMemberCount() + 1);

        List<Dog> dogs = dogRepository.findAllByParty(party);

        PartyAddPartyByIsbnResponse partyAddPartyByIsbnResponse = PartyAddPartyByIsbnResponse.of(party);
        partyAddPartyByIsbnResponse.updatePartyInfoResponse(dogs);

        return partyAddPartyByIsbnResponse;
    }
}
