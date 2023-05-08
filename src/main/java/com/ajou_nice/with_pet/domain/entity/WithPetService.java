package com.ajou_nice.with_pet.domain.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

//Service는 위드펫에서 기본적으로 제공하는 Service들을 말한다.
@Entity
@Getter @Setter
public class WithPetService {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_id")
	private Long id;

	private String name;

	private String introduction;
}
