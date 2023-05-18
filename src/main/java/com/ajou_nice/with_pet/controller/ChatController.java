package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.chat.ChatMainResponse;
import com.ajou_nice.with_pet.domain.dto.chat.ChatMessageRequest;
import com.ajou_nice.with_pet.domain.dto.chat.ChatMessageResponse;
import com.ajou_nice.with_pet.domain.dto.chat.ChatRoomRequest;
import com.ajou_nice.with_pet.domain.dto.chat.ChatRoomResponse;
import com.ajou_nice.with_pet.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;
	private final SimpMessagingTemplate template;


	//유저의 채팅방 목록 조회 (채팅방 목록 메인 페이지)
	@GetMapping("/chat/rooms")
	public Response<List<ChatMainResponse>> showMyRooms(@ApiIgnore Authentication authentication){
		List<ChatMainResponse> chatMainResponses = chatService.showRooms(authentication.getName());

		return Response.success(chatMainResponses);
	}

	//유저의 채팅룸 생성
	@PostMapping("/chat/room")
	public Response<ChatRoomResponse> createChatRoom(@ApiIgnore Authentication authentication,
			ChatRoomRequest chatRoomRequest){

		ChatRoomResponse chatRoomResponse = chatService.createChatRoom(authentication.getName(), chatRoomRequest);

		return Response.success(chatRoomResponse);
	}

	//유저의 채팅방 채팅 조회
	@GetMapping("/chat/room/{roomId}")
	public Response<ChatRoomResponse> showRoom(@ApiIgnore Authentication authentication,
			@PathVariable("roomId") Long roomId){

		ChatRoomResponse chatRoomResponse = chatService.getMessages(authentication.getName(),
				roomId);

		return Response.success(chatRoomResponse);
	}

	//채팅 보내기 -> authentication으로 senderId가 들어온다.
	@MessageMapping("/sendMessage/{roomId}")
	public Response<ChatMessageResponse> saveMessage(@ApiIgnore Authentication authentication,
			@RequestBody ChatMessageRequest chatMessageRequest, @DestinationVariable Long roomId){

		ChatMessageResponse chatMessageResponse = chatService.saveChat(authentication.getName(), chatMessageRequest, roomId);
		template.convertAndSend("/sub/chat/receive/chatroom", chatMessageResponse);

		return Response.success(chatMessageResponse);
	}
}
