package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHashTagRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "petsitter_hashtag")
public class PetSitterHashTag {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "petsitter_id")
	private PetSitter petSitter;

	private String hashTagName;

	public static PetSitterHashTag toEntity(PetSitter petSitter, PetSitterHashTagRequest petSitterHashTagRequest){
		return PetSitterHashTag.builder()
				.petSitter(petSitter)
				.hashTagName(petSitterHashTagRequest.getHashTagName())
				.build();
	}

}
