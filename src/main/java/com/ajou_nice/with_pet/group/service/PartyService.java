package com.ajou_nice.with_pet.group.service;

import com.ajou_nice.with_pet.group.model.dto.PartyInfoResponse;
import com.ajou_nice.with_pet.group.model.dto.PartyMemberRequest;
import com.ajou_nice.with_pet.group.model.dto.add.PartyAddRequest;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.group.model.dto.add.PartyAddResponse;
import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.group.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyService {

    private final Integer partyCount = 5;
    private final Integer partyMemberCount = 5;

    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final DogRepository dogRepository;
    private final ReservationRepository reservationRepository;

    private final ValidateCollection valid;

    @Transactional
    public PartyInfoResponse addMember(String email, PartyMemberRequest partyMemberRequest) {
        User user = userValidationByEmail(email);

        //그룹체크
        Party party = partyValidationByIsbn(partyMemberRequest.getPartyIsbn());

        //그룹에 멤버 추가 체크
        if (userPartyRepository.existsUserPartyByUserAndParty(user, party)) {
            throw new AppException(ErrorCode.DUPLICATED_GROUP_MEMBER,
                    ErrorCode.DUPLICATED_GROUP_MEMBER.getMessage());
        }

        if (party.getMemberCount() >= partyMemberCount) {
            throw new AppException(ErrorCode.TOO_MANY_MEMBER,
                    ErrorCode.TOO_MANY_MEMBER.getMessage());
        }

        //유저 그룹 매핑
        userPartyRepository.save(UserParty.of(user, party));
        user.updatePartyCount(user.getPartyCount() + 1);
        party.updateMemberCount(party.getMemberCount() + 1);

        List<Dog> dogs = dogRepository.findAllByParty(party);

        PartyInfoResponse partyInfoResponse = PartyInfoResponse.of(party);
        partyInfoResponse.updatePartyInfoResponse(dogs);
        return partyInfoResponse;
    }

    public List<PartyInfoResponse> getPartyInfoList(String userId) {

        valid.userValidationById(userId);

        List<Long> userPartyIdList = userPartyRepository.findAllUserPartyIdByUserId(userId);
        List<Party> partyList = partyRepository.findAllByUserPartyId(userPartyIdList);
        List<Dog> dogList = dogRepository.findAllUserDogs(userPartyIdList);

        List<PartyInfoResponse> partyInfoResponseList = partyList.stream()
                .map(PartyInfoResponse::of).collect(
                        Collectors.toList());

        for (PartyInfoResponse partyInfoResponse : partyInfoResponseList) {
            for (Dog dog : dogList) {
                log.info("dogId : {}", dog.getParty().getPartyId());
                if (partyInfoResponse.getPartyId() == dog.getParty().getPartyId()) {
                    partyInfoResponse.updatePartyInfoResponse(dog);
                }
            }
        }

        return partyInfoResponseList;
    }

    @Transactional
    public PartyAddResponse createParty(String email, PartyAddRequest partyAddRequest) {
        // 파티 생성할 때 반려견을 등록해야 함
        User user = userValidationByEmail(email);

        Party party = userPartyCountCheckAndGenerationParty(user, partyAddRequest);

        Dog dog = registerPartyDog(party, partyAddRequest);

        return PartyAddResponse.of(dog);
    }

    private String createInvitationCode(Party party) {
        return RandomStringUtils.randomAlphabetic(6) + party.getPartyId().toString();
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

    private Party userPartyCountCheckAndGenerationParty(User partyLeader, PartyAddRequest partyAddRequest) {

        if (partyLeader.getPartyCount() >= partyCount) {
            throw new AppException(ErrorCode.TOO_MANY_GROUP, ErrorCode.TOO_MANY_GROUP.getMessage());
        }

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
}
