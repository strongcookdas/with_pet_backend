package com.ajou_nice.with_pet.domain.entity;


import com.ajou_nice.with_pet.enums.DogSize;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetSitter extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "petsitter_id")
	private Long id;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="applicant_id",unique = true, nullable = false)
	private PetSitterApplicant applicant;

	@OneToMany(mappedBy = "petSitter")
	private List<PetSitterWithPetService> petSitterWithPetServiceList = new ArrayList<PetSitterWithPetService>();

	@OneToMany(mappedBy = "petSitter")
	private List<House> petSitterHouseList = new ArrayList<House>();

	@OneToMany(mappedBy = "petSitter")
	private List<PetSitterHashTag> petSitterHashTagList = new ArrayList<PetSitterHashTag>();

	@Enumerated(EnumType.STRING)
	private DogSize availableDogSize;

	@Lob
	private String introduction;

	private Boolean valid;

	public void changeValidation(Boolean valid){
		this.valid = valid;
	}

	public void updateIntroduction(String introduction){
		this.introduction = introduction;
	}

	public static PetSitter toEntity(PetSitterApplicant petSitterApplicant){

		return PetSitter.builder()
				.applicant(petSitterApplicant)
				.valid(false)
				.build();
	}

}
