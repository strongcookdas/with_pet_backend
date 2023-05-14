package com.ajou_nice.with_pet.domain.entity;


import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterHouseRequest;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class House {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "petsitter_id", nullable = true)
	private PetSitter petSitter;

	private String house_img;

	private Boolean representative;


	public static House toEntity(PetSitter petSitter,PetSitterHouseRequest petSitterHouseRequest){
		return House.builder()
				.petSitter(petSitter)
				.house_img(petSitterHouseRequest.getHouseImg())
				.representative(petSitterHouseRequest.getRepresentative())
				.build();
	}



}
