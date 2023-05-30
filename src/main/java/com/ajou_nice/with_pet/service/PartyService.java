package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.dog.DogInfoResponse;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyService {

    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final DogRepository dogRepository;

    // 새로운 그룹생성 (그룹 이름이 없는 버전)


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

        log.info("=============================== Party START ===============================");
        List<Party> partyList = partyRepository.findAllByUserPartyId(userPartyIdList);
        log.info("=============================== Party END ===============================");

        log.info("=============================== Dog START ===============================");
        List<Dog> dogList = dogRepository.findAllUserDogs(userPartyIdList);
        log.info("=============================== Dog END ===============================");

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
        //파티 생성
        Party party = Party.of(user, partyRequest.getPartyName());
        party = partyRepository.save(party);
        party.updateParty(party.getPartyId().toString());
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
        dogRepository.save(dog);
        return PartyInfoResponse.of(dog);
    }
}
