package com.ajou_nice.with_pet.domain.dto.chat;


import com.ajou_nice.with_pet.domain.entity.ChatRoom;
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
public class ChatMainResponse {

	private Long chatRoomId;
	private String otherProfileImg;
	private String otherName;
	private LocalDateTime recentMessageTime;
	private int notReceivedCount;



	//유저에게는 other이 petSitter
	public static List<ChatMainResponse> toList(List<ChatRoom> chatRooms){
		return chatRooms.stream().map(chatRoom -> ChatMainResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.otherProfileImg(chatRoom.getOther().getProfileImg())
				.otherName(chatRoom.getOther().getName())
				.recentMessageTime(chatRoom.getLastModifiedTime())
				.notReceivedCount(chatRoom.getAllMessageCount() - chatRoom.getShowMyMessageCount())
				.build()).collect(Collectors.toList());
	}

	public static List<ChatMainResponse> forPetSitterList(List<ChatRoom> chatRooms){
		return chatRooms.stream().map(chatRoom -> ChatMainResponse.builder()
				.chatRoomId(chatRoom.getRoomId())
				.otherProfileImg(chatRoom.getMe().getProfileImg())
				.otherName(chatRoom.getMe().getName())
				.recentMessageTime(chatRoom.getLastModifiedTime())
				.notReceivedCount(chatRoom.getAllMessageCount() - chatRoom.getShowOtherMessageCount())
				.build()).collect(Collectors.toList());
	}
}
