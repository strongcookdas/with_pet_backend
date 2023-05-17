package com.ajou_nice.with_pet.domain.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
public class ChatRoom {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomId;
	private String roomName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "petsitter_id")
	private PetSitter petSitter;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;


	public static ChatRoom toEntity(String roomName){
		return ChatRoom.builder()
				.roomName(roomName)
				.build();
	}
}
