package com.ajou_nice.with_pet.domain.dto.chat;


import com.ajou_nice.with_pet.domain.entity.ChatMessage;
import com.ajou_nice.with_pet.domain.entity.ChatRoom;
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
public class ChatRoomResponse {

	private Long chatRoomId;

	private List<ChatMessageResponse> chatMessages;


	public static ChatRoomResponse of(ChatRoom chatRoom, List<ChatMessageResponse> chatMessages){
		return ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.chatMessages(chatMessages)
				.build();
	}
}
