package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final BCryptPasswordEncoder encoder;

    // 새로운 그룹생성 (그룹 이름이 없는 버전)
    @Transactional
    public Party createParty(User user) {
        Party party = partyRepository.save(new Party(user));
        party.updateParty("", party.getPartyId().toString(), encoder);
        return party;
    }
}
