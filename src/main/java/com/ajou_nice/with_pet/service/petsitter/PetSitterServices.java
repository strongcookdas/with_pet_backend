package com.ajou_nice.with_pet.service.petsitter;

import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetSitterServices {

	private final PetSitterRepository petSitterRepository;

	/*
		PetSitter가 Service 선택 후 저장
		input -> petsitter id, WithPetService Set {Service : price}
	 */
	public void createPetSitterService(Long petSitterId){
		//엔티티 조회
		PetSitter petSitter = petSitterRepository.findById(petSitterId).orElseThrow(()-> {
			throw new RuntimeException("잘못된 요청");
		});
	}

	/*
		PetSitter가
	 */
}
