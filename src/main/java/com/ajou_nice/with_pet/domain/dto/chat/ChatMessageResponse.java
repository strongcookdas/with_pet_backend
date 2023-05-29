package com.ajou_nice.with_pet.domain.dto.chat;

import com.ajou_nice.with_pet.domain.entity.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
	private String senderId;
	private LocalDateTime sendTime;

	public static ChatMessageResponse of(ChatMessage chatMessage){
		return ChatMessageResponse.builder()
				.message(chatMessage.getMessage())
				.senderId(chatMessage.getSenderId())
				.sendTime(chatMessage.getSendTime())
				.build();
	}

	public static List<ChatMessageResponse> toList(List<ChatMessage> chatMessages){
		return chatMessages.stream().map(chatMessage -> ChatMessageResponse.builder()
				.message(chatMessage.getMessage())
				.senderId(chatMessage.getSenderId())
				.sendTime(chatMessage.getSendTime())
				.build()).collect(Collectors.toList());
	}
}
