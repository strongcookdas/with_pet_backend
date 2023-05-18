package com.ajou_nice.with_pet.domain.dto.chat;


import com.ajou_nice.with_pet.domain.entity.ChatRoom;
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
public class ChatRoomResponse {

	private Long chatRoomId;
	private String userId;
	private String senderProfileImg;

	private Long senderId;

	private String senderName;

	public static ChatRoomResponse of(ChatRoom chatRoom){

		return ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.senderProfileImg(chatRoom.getSender().getApplicant().getUser().getProfileImg())
				.petSitterId(chatRoom.getPetSitter().getId())
				.petSitterName(chatRoom.getPetSitter().getApplicant().getUser().getName())
				.build();

	}

	public static List<ChatRoomResponse> toList(List<ChatRoom> chatRooms){
		return chatRooms.stream().map(chatRoom -> ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.petSitterProfileImg(chatRoom.getPetSitter().getApplicant().getUser().getProfileImg())
				.petSitterId(chatRoom.getPetSitter().getId())
				.petSitterName(chatRoom.getPetSitter().getApplicant().getUser().getName())
				.build()).collect(Collectors.toList());
	}
}
