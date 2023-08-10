package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHashTagRequest;
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
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "hashtag")
public class HashTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "petSitterId")
	private PetSitter petSitter;

	private String name;

	public static HashTag toEntity(PetSitter petSitter, PetSitterHashTagRequest petSitterHashTagRequest){
		return HashTag.builder()
				.petSitter(petSitter)
				.name(petSitterHashTagRequest.getHashTagName())
				.build();
	}

}
