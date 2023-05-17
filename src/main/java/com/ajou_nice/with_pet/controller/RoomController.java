package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.service.ChatRoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

	//채팅방 확인을 위한 controller
	private final ChatRoomService chatRoomService;

	//채팅방 목록 조회
	@GetMapping(value = "chat/rooms")
	@ApiOperation("채팅방 목록 조회")
	public

	//채팅방 개설
	@PostMapping(value = "chat/room")
	public Response createRoom(@RequestParam String )

}
