package com.ajou_nice.with_pet.domain.dto.chat;

import com.ajou_nice.with_pet.domain.entity.ChatMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ChatMessageResponse {

	private String message;
	private String writerId;

	public static ChatMessageResponse of(ChatMessage chatMessage){
		return ChatMessageResponse.builder()
				.message(chatMessage.getMessage())
				.writerId(chatMessage.getWriterId())
				.build();
	}

	public static List<ChatMessageResponse> toList(List<ChatMessage> chatMessages){

	}
}
