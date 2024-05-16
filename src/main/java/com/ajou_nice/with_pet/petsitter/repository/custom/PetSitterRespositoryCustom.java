package com.ajou_nice.with_pet.petsitter.repository.custom;

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetSitterRespositoryCustom {
	Page<PetSitter> searchPage(Pageable pageable, String dogSize, List<String> service, String address);
}
