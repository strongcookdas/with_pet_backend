package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterMainDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetSitterRespositoryCustom {
	Page<PetSitterMainDto> searchPage(Pageable pageable);
}
