package com.ajou_nice.with_pet.group.service;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyUserValidationService {
    private final UserPartyRepository userPartyRepository;

    public void validationPartyUser(User user, Party party) {
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, party)) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "해당 그룹 권한이 없습니다.");
        }
    }
}
