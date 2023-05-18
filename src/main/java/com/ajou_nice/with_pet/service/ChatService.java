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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final ChatMessageRepository chatMessageRepository;

	//채팅방 목록 조회
	public List<ChatMainResponse> showRooms(String userId){

		User me = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		List<ChatRoom> myChatRooms = chatRoomRepository.findChatRoomByMyId(me.getId());

		return ChatMainResponse.toList(myChatRooms);

	}

	@Transactional
	// 채팅룸 생성 (유저만 생성 가능)
	public ChatRoomResponse createChatRoom(String userId, ChatRoomRequest chatRoomRequest){
		User me = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		User other = userRepository.findById(chatRoomRequest.getOtherId()).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		ChatRoom newChatRoom = ChatRoom.toEntity(me, other, chatRoomRequest.getCreateTime());
		chatRoomRepository.save(newChatRoom);
		List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomOrderBySendTimeAsc(newChatRoom);

		return ChatRoomResponse.of(newChatRoom, ChatMessageResponse.toList(messages));
	}

	//채팅방 채팅들 불러오기
	public ChatRoomResponse getMessages(String userId, Long chatRoomId){
		User me = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()->{
			throw new AppException(ErrorCode.CHATROOM_NOT_FOUND, ErrorCode.CHATROOM_NOT_FOUND.getMessage());
		});

		List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomOrderBySendTimeAsc(chatRoom);

		//마지막 내가 본 시점 수정 + showMessageCount ++
		chatRoom.updateMyLastShowTime(LocalDateTime.now());

		return ChatRoomResponse.of(chatRoom, ChatMessageResponse.toList(messages));
	}

	@Transactional
	//채팅 DB에 저장
	public ChatMessageResponse saveChat(String userId, ChatMessageRequest chatMessageRequest, Long roomId){

		//유저 검증
		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		//채팅룸이 존재하는지 확인
		ChatRoom findRoom = chatRoomRepository.findById(roomId).orElseThrow(()->{
			throw new AppException(ErrorCode.CHATROOM_NOT_FOUND, ErrorCode.CHATROOM_NOT_FOUND.getMessage());
		});

		ChatMessage chatMessage = ChatMessage.toEntity(chatMessageRequest,findRoom,userId);
		chatMessageRepository.save(chatMessage);

		//채팅룸 마지막으로 내가 확인한 시간과 업데이트 시간을 업데이트
		// + 모든 메시지 count ++, showMessageCount ++
		findRoom.updateModifiedTime(chatMessageRequest.getSendTime());

		return ChatMessageResponse.of(chatMessage);
	}

}
