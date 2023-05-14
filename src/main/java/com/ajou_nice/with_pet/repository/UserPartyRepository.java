package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {
    //리팩토링이 필요할 거 같다.
    boolean existsUserPartyByUserAndParty(User user, Party party);
}
