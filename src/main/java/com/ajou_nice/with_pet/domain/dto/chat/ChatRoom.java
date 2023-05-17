package com.ajou_nice.with_pet.domain.dto.chat;


import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class ChatRoom {

	private String roomId;
	private String name;
	private Set<WebSocketSession> sessions = new HashSet<>();


}
