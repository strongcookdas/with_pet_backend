package com.ajou_nice.with_pet.repository.custom;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetSitterRespositoryCustom {
	Page<PetSitter> searchPage(Pageable pageable);
}
