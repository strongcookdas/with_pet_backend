package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.chat.ChatMainResponse;
import com.ajou_nice.with_pet.domain.dto.chat.ChatMessageRequest;
import com.ajou_nice.with_pet.domain.dto.chat.ChatMessageResponse;
import com.ajou_nice.with_pet.domain.dto.chat.ChatRoomRequest;
import com.ajou_nice.with_pet.domain.dto.chat.ChatRoomResponse;
import com.ajou_nice.with_pet.domain.entity.ChatMessage;
import com.ajou_nice.with_pet.domain.entity.ChatRoom;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.ChatMessageRepository;
import com.ajou_nice.with_pet.repository.ChatRoomRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final ChatMessageRepository chatMessageRepository;

	private final PetSitterRepository petSitterRepository;
	private final ValidateCollection valid;

	//채팅방 목록 조회
	public List<ChatMainResponse> showRooms(String userId){

		User me = valid.userValidation(userId);

		List<ChatRoom> myChatRooms = chatRoomRepository.findChatRoomByMyId(me.getUserId());
		if(myChatRooms.isEmpty()){
			myChatRooms = chatRoomRepository.findChatRoomByOtherId(me.getUserId());
			return ChatMainResponse.forPetSitterList(myChatRooms);
		}
		else {
			return ChatMainResponse.toList(myChatRooms);
		}
	}

	@Transactional
	// 채팅룸 생성 (유저만 생성 가능)
	public ChatRoomResponse createChatRoom(String userId, ChatRoomRequest chatRoomRequest){
		User me = valid.userValidation(userId);

		User other = valid.userValidation(chatRoomRequest.getOtherId());

		Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByMeAndOther(me, other);
		//이미 존재한다면 기존의 chatRoom에 대한 response return
		if(!chatRoom.isEmpty()){
			return ChatRoomResponse.of(chatRoom.get());
		}
		// 존재하지 않았다면 새로 생성한 후 새로운 chatRoom에 대한 response return
		else{
			ChatRoom newChatRoom = ChatRoom.toEntity(me, other, chatRoomRequest.getCreateTime());

			chatRoomRepository.save(newChatRoom);
			return ChatRoomResponse.of(newChatRoom);
		}
	}

	//채팅방 채팅들 불러오기
	public ChatRoomResponse getMessages(String userId, Long chatRoomId){

		User me = valid.userValidation(userId);
		ChatRoom chatRoom = valid.chatRoomValidation(chatRoomId);

		List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomOrderBySendTimeAsc(chatRoom);


		Optional<PetSitter> existPetSitter = petSitterRepository.findByUser(me);

		//펫시터일때
		if(!existPetSitter.isEmpty()){
			chatRoom.updateOtherLastShowTime(LocalDateTime.now());
			return ChatRoomResponse.ofPetSitter(chatRoom, ChatMessageResponse.toList(messages));
		}
		else{
			//마지막 내가 본 시점 수정 + showMessageCount ++
			chatRoom.updateMyLastShowTime(LocalDateTime.now());
			return ChatRoomResponse.of(chatRoom, ChatMessageResponse.toList(messages));
		}
	}

	@Transactional
	//채팅 DB에 저장
	public ChatMessageResponse saveChat(String userId, ChatMessageRequest chatMessageRequest, Long roomId){

		//유저 검증
		User findUser = valid.userValidation(userId);

		//채팅룸이 존재하는지 확인
		ChatRoom findRoom = valid.chatRoomValidation(roomId);

		ChatMessage chatMessage = ChatMessage.toEntity(chatMessageRequest,findRoom,findUser.getUserId());
		chatMessageRepository.save(chatMessage);

		Optional<PetSitter> existPetSitter = petSitterRepository.findByUser(findUser);

		//펫시터일때
		if(!existPetSitter.isEmpty()){
			//채팅룸 마지막으로 내가 확인한 시간과 업데이트 시간을 업데이트
			// + 모든 메시지 count ++, showMessageCount ++
			findRoom.updateOtherModifiedTime(chatMessageRequest.getSendTime());
		}
		else{
			//채팅룸 마지막으로 내가 확인한 시간과 업데이트 시간을 업데이트
			// + 모든 메시지 count ++, showMessageCount ++
			findRoom.updateMyModifiedTime(chatMessageRequest.getSendTime());
		}

		return ChatMessageResponse.of(chatMessage);
	}

}
