package com.ajou_nice.with_pet.domain.dto.chat;


import lombok.Getter;

@Getter
public class ChatMessage {

	private String roomId;

	private String writer;

	private String message;
}
