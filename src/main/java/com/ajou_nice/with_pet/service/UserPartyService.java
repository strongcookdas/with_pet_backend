package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPartyService {

    private final UserPartyRepository userPartyRepository;

    public void addUserParty(User user, Party party) {
        userPartyRepository.save(UserParty.of(user, party));
    }

}
