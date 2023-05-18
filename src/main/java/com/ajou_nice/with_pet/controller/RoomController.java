package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.service.ChatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

	//채팅방 확인을 위한 controller
	private final ChatService chatRoomService;

	//채팅방 목록 조회 (유저)
	@GetMapping(value = "chat/rooms/{userId}")
	@ApiOperation("채팅방 목록 조회")
	public Response<> showChatRoom(@ApiIgnore Authentication authentication,
			@RequestParam())

	//채팅방 개설
	@PostMapping(value = "chat/room")
	public Response createRoom(@RequestParam String )

}
