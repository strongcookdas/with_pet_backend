package com.ajou_nice.with_pet.domain.dto.chat;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {

	private Long roomId;

	private String writer;

	private String message;
}
