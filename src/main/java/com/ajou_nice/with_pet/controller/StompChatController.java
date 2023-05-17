package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.chat.ChatMessageRequest;
import com.ajou_nice.with_pet.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StompChatController {

	private final SimpMessagingTemplate template;

	//client가 send할 수 있는 경로
	//"/pub/chat/enter"
	@MessageMapping(value = "/chat/enter")
	public void enter(ChatMessageRequest chatMessageRequest){
		chatMessageRequest.setMessage(chatMessageRequest.getWriter() + "님이 입장하였습니다.");
		template.convertAndSend("/sub/chat/room/"+ chatMessageRequest.getRoomId(), chatMessageRequest);
	}

	@MessageMapping(value = "/chat/message")
	public void message(ChatMessageRequest chatMessageRequest){
		template.convertAndSend("/sub/chat/room/" + chatMessageRequest.getRoomId(), chatMessageRequest);
	}
}
