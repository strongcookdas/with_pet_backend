package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {

    //리팩토링이 필요할 거 같다.
    boolean existsUserPartyByUserAndParty(User user, Party party);

    @Query("select u.party.partyId from UserParty u where u.user.id=:userId")
    List<Long> findAllUserPartyIdByUserId(@Param("userId") String userId);

    @Query("select u from UserParty u where u.party.partyId in (:partyIdList) order by u.party.partyId asc ")
    List<UserParty> findAllUserPartyByPartyId(@Param("partyIdList") List<Long> partyIdList);

    @Query("select u from UserParty u join fetch u.user where u.party.partyId =:partyId and u.user.userId <>:userId ")
    List<UserParty> findAllByPartyAndUser(@Param("partyId") Long partyId,
            @Param("userId") Long userId);
}
