package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Party;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findByPartyIsbn(String isbn);

    @Query("select distinct p from Party p join fetch p.user join fetch p.userPartyList where p.partyId in (:userPartyIdList) order by p.partyId")
    List<Party> findAllByUserPartyId(@Param("userPartyIdList") List<Long> userPartyIdList);
}
