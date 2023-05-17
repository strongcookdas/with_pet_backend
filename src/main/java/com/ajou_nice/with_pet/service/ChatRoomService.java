package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	//채팅방 목록 조회
	public Response showRooms(){

	}
}
