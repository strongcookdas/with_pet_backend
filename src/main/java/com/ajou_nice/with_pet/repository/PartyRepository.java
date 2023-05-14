package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Party;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findByPartyIsbn(String isbn);
}
