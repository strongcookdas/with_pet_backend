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
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyService {

    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final DogRepository dogRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public PartyAddPartyByIsbnResponse addPartyByPartyIsbn(String email, PartyAddPartyByIsbnRequest partyAddPartyByIsbnRequest) {
        User user = userValidationByEmail(email);
        Party party = partyValidationByIsbn(partyAddPartyByIsbnRequest.getPartyIsbn());

        checkDuplicatedUserInParty(user, party);
        checkUserPartyCount(user);
        checkPartyMemberCount(party);

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

        checkUserPartyCount(user);
        Party party = generateParty(user, partyAddRequest);
        Dog dog = registerPartyDog(party, partyAddRequest);

        return PartyAddResponse.of(dog);
    }

    @Transactional
    public String leaveParty(String email, Long partyId) {
        User user = userValidationByEmail(email);
        Party party = partyValidationById(partyId);

        UserParty deleteUserParty = checkUserInParty(user, party);
        checkUserReservationInParty(user, party);

        deleteMemberInParty(user, party, deleteUserParty);
        return processDeleteParty(user, party);
    }

    @Transactional
    public String expelMember(String email, Long partyId, Long memberId) {

        User leader = userValidationByEmail(email);
        User member = userValidationById(memberId);
        Party party = partyValidationById(partyId);

        checkPartyLeaderValidation(leader, party);
        checkUserReservationInParty(member, party);

        UserParty userParty = checkUserInParty(member, party);
        deleteMemberInParty(member, party, userParty);

        return member.getName() + "님이 그룹에서 방출되었습니다.";
    }

    private User userValidationByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    private User userValidationById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    private Party partyValidationByIsbn(String isbn) {
        return partyRepository.findByPartyIsbn(isbn).orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, ErrorCode.GROUP_NOT_FOUND.getMessage()));
    }

    private Party partyValidationById(Long partyId) {
        return partyRepository.findById(partyId).orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, ErrorCode.GROUP_NOT_FOUND.getMessage()));
    }

    private void checkUserPartyCount(User user) {
        Integer PARTY_MAX_COUNT = 5;
        if (user.getPartyCount() >= PARTY_MAX_COUNT) {
            throw new AppException(ErrorCode.TOO_MANY_GROUP, ErrorCode.TOO_MANY_GROUP.getMessage());
        }
    }

    private void checkPartyLeaderValidation(User user, Party party) {
        if (!user.getId().equals(party.getPartyLeader().getId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "멤버를 방출시킬 권한이 없습니다.");
        }
    }

    private Party generateParty(User partyLeader, PartyAddRequest partyAddRequest) {
        Party party = Party.of(partyLeader, partyAddRequest.getPartyName());
        party = partyRepository.save(party);

        UserParty userParty = UserParty.of(partyLeader, party);
        userPartyRepository.save(userParty);
        partyLeader.updatePartyCount(partyLeader.getPartyCount() + 1);

        return party;
    }

    private UserParty checkUserInParty(User user, Party party) {
        Optional<UserParty> userParty = userPartyRepository.findByUserAndParty(user, party);
        if (userParty.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_GROUP_MEMBER, ErrorCode.NOT_FOUND_GROUP_MEMBER.getMessage());
        }
        return userParty.get();
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
        List<Long> userPartyList = userPartyRepository.findAllUserPartyIdByUserId(email);
        List<Party> partyList = partyRepository.findAllByUserPartyId(userPartyList);
        List<Dog> dogList = dogRepository.findAllUserDogs(userPartyList);

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

    private boolean isExistUserInParty(User user, Party party) {
        return (userPartyRepository.existsUserPartyByUserAndParty(user, party));
    }

    private void checkDuplicatedUserInParty(User user, Party party) {
        if (isExistUserInParty(user, party))
            throw new AppException(ErrorCode.DUPLICATED_GROUP_MEMBER, ErrorCode.DUPLICATED_GROUP_MEMBER.getMessage());
    }

    private void checkPartyMemberCount(Party party) {
        Integer PARTY_MEMBER_MAX_COUNT = 5;
        if (party.getMemberCount() >= PARTY_MEMBER_MAX_COUNT) {
            throw new AppException(ErrorCode.TOO_MANY_MEMBER, ErrorCode.TOO_MANY_MEMBER.getMessage());
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

    private void checkUserReservationInParty(User user, Party party) {
        List<Dog> dogs = dogRepository.findAllByParty(party);
        if (reservationRepository.existsByUserAndDogInAndReservationStatusIn(user, dogs, Arrays.asList(ReservationStatus.values()))) {
            throw new AppException(ErrorCode.CAN_NOT_LEAVE_PARTY, ErrorCode.CAN_NOT_LEAVE_PARTY.getMessage());
        }
    }

    private void deleteMemberInParty(User user, Party party, UserParty userParty) {
        userPartyRepository.delete(userParty);
        user.updatePartyCount(user.getPartyCount() - 1);
        party.updateMemberCount(party.getMemberCount() - 1);
    }

    private String processDeleteParty(User user, Party party) {
        Optional<UserParty> nextLeader = userPartyRepository.findFirstByParty(party);

        if (userPartyRepository.findAllByParty(party).isEmpty()) {
            List<Dog> dogs = party.getDogList();
            dogRepository.deleteAll(dogs);
            partyRepository.delete(party);
        } else if (party.getPartyLeader().getId().equals(user.getId()) && nextLeader.isPresent()) {
            party.updatePartyLeader(nextLeader.get().getUser());
            return nextLeader.get().getUser().getName() + "님이 방장이 되었습니다.";
        }

        return "그룹을 탈퇴했습니다.";
    }
}
