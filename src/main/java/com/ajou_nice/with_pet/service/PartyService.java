package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.party.PartyInfoResponse;
import com.ajou_nice.with_pet.domain.dto.party.PartyMemberRequest;
import com.ajou_nice.with_pet.domain.dto.party.PartyRequest;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final DogRepository dogRepository;

    @Transactional
    public PartyInfoResponse addMember(String userId, PartyMemberRequest partyMemberRequest) {
        //유저체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        //그룹체크
        Party party = partyRepository.findByPartyIsbn(partyMemberRequest.getPartyIsbn())
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.GROUP_NOT_FOUND,
                            ErrorCode.GROUP_NOT_FOUND.getMessage());
                });

        //그룹에 멤버 추가 체크
        if (userPartyRepository.existsUserPartyByUserAndParty(user, party)) {
            throw new AppException(ErrorCode.DUPLICATED_GROUP_MEMBER,
                    ErrorCode.DUPLICATED_GROUP_MEMBER.getMessage());
        }

        //유저 그룹 매핑
        userPartyRepository.save(UserParty.of(user, party));
        user.updatePartyCount(user.getPartyCount() + 1);

        List<Dog> dogs = dogRepository.findAllByParty(party);

        PartyInfoResponse partyInfoResponse = PartyInfoResponse.of(party);
        partyInfoResponse.updatePartyInfoResponse(dogs);
        return partyInfoResponse;
    }

    public List<PartyInfoResponse> getPartyInfoList(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

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
    public PartyInfoResponse createParty(String userId, PartyRequest partyRequest) {
        //유저체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        if (user.getPartyCount() >= partyCount) {
            throw new AppException(ErrorCode.TOO_MANY_GROUP, ErrorCode.TOO_MANY_GROUP.getMessage());
        }

        //파티 생성
        Party party = Party.of(user, partyRequest.getPartyName());
        party = partyRepository.save(party);

        //파티 코드 생성
        party.updatePartyIsbn(createInvitationCode(party));

        //유저 파티 매핑
        UserParty userParty = UserParty.of(user, party);
        userPartyRepository.save(userParty);

        //반려견 등록
        DogSize myDogSize;
        if (partyRequest.getDog_weight() > 18) {
            myDogSize = DogSize.대형견;
        } else if (partyRequest.getDog_weight() > 10) {
            myDogSize = DogSize.중형견;
        } else {
            myDogSize = DogSize.소형견;
        }

        Dog dog = Dog.of(partyRequest, party, myDogSize);
        dog = dogRepository.save(dog);

        user.updatePartyCount(user.getPartyCount() + 1);

        return PartyInfoResponse.of(dog);
    }

    private String createInvitationCode(Party party) {
        return RandomStringUtils.randomAlphabetic(6) + party.getPartyId().toString();
    }

    @Transactional
    public String leaveParty(String userId, Long partyId) {

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        Party party = partyRepository.findById(partyId).orElseThrow(() -> {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND,
                    ErrorCode.GROUP_NOT_FOUND.getMessage());
        });

        Optional<UserParty> deleteUserParty = userPartyRepository.findByUserAndParty(user, party);
        if (deleteUserParty.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_GROUP_MEMBER,
                    ErrorCode.NOT_FOUND_GROUP_MEMBER.getMessage());
        }

        Optional<UserParty> nextLeader = userPartyRepository.findFirstByUserNotAndParty(user,
                party);

        userPartyRepository.delete(deleteUserParty.get());
        user.updatePartyCount(user.getPartyCount() - 1);

        if (party.getUser().getId().equals(user.getId())
                && !nextLeader.isEmpty()) {
            party.updatePartyLeader(
                    userPartyRepository.findFirstByUserNotAndParty(user, party).get().getUser());
            return nextLeader.get().getUser().getName() + "님이 방장이 되었습니다.";
        }

        return "그룹에서 탈퇴되었습니다.";
    }

    @Transactional
    public String expelMember(String userId, Long partyId, Long memberId) {
        User leader = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        User member = userRepository.findById(memberId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        Party party = partyRepository.findById(partyId).orElseThrow(() -> {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND,
                    ErrorCode.GROUP_NOT_FOUND.getMessage());
        });

        if (!leader.getId().equals(party.getUser().getId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "멤버를 방출시킬 권한이 없습니다.");
        }

        Optional<UserParty> userParty = userPartyRepository.findByUserAndParty(member, party);
        if (userParty.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_GROUP_MEMBER,
                    ErrorCode.NOT_FOUND_GROUP_MEMBER.getMessage());
        }

        userPartyRepository.delete(userParty.get());
        member.updatePartyCount(member.getPartyCount() - 1);

        return member.getName() + "님이 그룹에서 방출되었습니다";
    }
}
