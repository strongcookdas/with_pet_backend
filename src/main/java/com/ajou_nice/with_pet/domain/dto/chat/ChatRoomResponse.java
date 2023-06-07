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
	private String myName;
	private String otherName;


	public static ChatRoomResponse of(ChatRoom chatRoom, List<ChatMessageResponse> chatMessages){
		return ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.myName(chatRoom.getMe().getName())
				.otherName(chatRoom.getOther().getName())
				.chatMessages(chatMessages)
				.build();
	}

	public static ChatRoomResponse ofPetSitter(ChatRoom chatRoom, List<ChatMessageResponse> chatMessages){
		return ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.myName(chatRoom.getOther().getName())
				.otherName(chatRoom.getMe().getName())
				.chatMessages(chatMessages)
				.build();
	}

	public static ChatRoomResponse of(ChatRoom chatRoom){
		return ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.myName(chatRoom.getMe().getName())
				.otherName(chatRoom.getOther().getName())
				.build();
	}
}
