package com.ajou_nice.with_pet.domain.dto.chat;


import com.ajou_nice.with_pet.domain.entity.ChatRoom;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
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
	private String otherProfileImg;
	private String otherName;
	private String recentMessage;	//원래 메세지의 최신 substring

	//반려인 입장에서 response(채팅방에서 펫시터의 정보가 들어오면 된다)
	public static ChatRoomResponse ofUser(ChatRoom chatRoom, String recentMessage){

		return ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.otherProfileImg(chatRoom.getPetSitter().getApplicant().getUser().getProfileImg())
				.otherName(chatRoom.getPetSitter().getApplicant().getUser().getName())
				.recentMessage(recentMessage)
				.build();
	}

	// 펫시터 입장에서 response (채팅방에서 user의 정보가 들어오면 된다)
	public static ChatRoomResponse ofPetSitter(ChatRoom chatRoom, String recentMessage){
		return ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.otherProfileImg(chatRoom.getUser().getProfileImg())
				.otherName(chatRoom.getUser().getName())
				.recentMessage(recentMessage)
				.build();
	}

	//유저에게는 other이 petSitter이기 때문
	public static List<ChatRoomResponse> toListforUser(List<ChatRoom> chatRooms){
		return chatRooms.stream().map(chatRoom -> ChatRoomResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.otherProfileImg(chatRoom.getPetSitter().getApplicant().getUser().getProfileImg())
				.otherName(chatRoom.getPetSitter().getApplicant().getUser().getName())
				.recentMessage
				.
	}
}
