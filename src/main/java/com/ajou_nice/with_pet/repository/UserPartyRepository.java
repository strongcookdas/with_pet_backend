package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {

    //리팩토링이 필요할 거 같다.
    boolean existsUserPartyByUserAndParty(User user, Party party);

    @Query("select u.party.partyId from UserParty u where u.user.id=:userId")
    List<Long> findAllUserPartyIdByUserId(@Param("userId") String userId);

    @Query("select u from UserParty u join fetch u.user where u.party.partyId =:partyId and u.user.id <>:userId ")
    List<UserParty> findAllByPartyAndUser(@Param("partyId") Long partyId,
            @Param("userId") Long userId);

    @Query("select u from UserParty u join fetch u.user where u.party =:party")
    List<UserParty> findAllByParty(@Param("party") Party party);
    Optional<UserParty> findFirstByUserNotAndParty(User user, Party party);

    Optional<UserParty> findByUserAndParty(User user, Party party);
}
