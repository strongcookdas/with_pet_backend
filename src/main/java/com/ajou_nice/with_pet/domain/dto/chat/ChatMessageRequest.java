package com.ajou_nice.with_pet.domain.dto.chat;


import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatMessageRequest {

	private String writerId;

	@Lob
	private String message;
}