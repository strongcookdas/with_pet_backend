package com.ajou_nice.with_pet.dog.repository;

import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.group.model.entity.Party;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DogRepository extends JpaRepository<Dog, Long>, DogRepositoryCustom {

    @Query("select distinct d from Dog d join  fetch d.party where d.party.partyId in (:userPartyList) order by d.party.partyId asc ")
    List<Dog> findAllUserDogs(@Param("userPartyList") List<Long> userPartyList);

    List<Dog> findAllByParty(Party party);
}
