package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.party.PartyMemberRequest;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final BCryptPasswordEncoder encoder;

    // 새로운 그룹생성 (그룹 이름이 없는 버전)
    @Transactional
    public Party createParty(User user) {
        Party party = partyRepository.save(new Party(user));
        party.updateParty("", party.getPartyId().toString());
        return party;
    }

    public void addMember(String userId, PartyMemberRequest partyMemberRequest) {
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
        if(userPartyRepository.existsUserPartyByUserAndParty(user,party)){
            throw new AppException(ErrorCode.DUPLICATED_GROUP_MEMBER,ErrorCode.DUPLICATED_GROUP_MEMBER.getMessage());
        }
        //유저 그룹 매핑
        userPartyRepository.save(UserParty.of(user, party));
    }
}
