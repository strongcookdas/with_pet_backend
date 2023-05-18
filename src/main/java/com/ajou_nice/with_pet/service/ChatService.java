package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.entity.ChatRoom;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.ChatRoomRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final PetSitterRepository petSitterRepository;

	//채팅방 목록 조회
	public Response showRooms(String userId){

		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		List<ChatRoom> chatRoomList = new ArrayList<>();

		//반려인이라면 chatRoom response가
		if(findUser.getRole().equals(UserRole.ROLE_USER)){
			chatRoomList = chatRoomRepository.findChatRoomByUserId(findUser.getUserId());
		}
		else if(findUser.getRole().equals(UserRole.ROLE_PETSITTER)){
			PetSitter petSitter = petSitterRepository.findByPetSitterByUserId(findUser.getUserId()).orElseThrow(()->{
				throw new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage());
			});
			chatRoomList = chatRoomRepository.findChatRoomByPetSitterId(petSitter.getId());
		}

		//
		if(!chatRoomList.isEmpty()){

		}
	}



	//한 채팅방의 채팅들 불러오기

}
