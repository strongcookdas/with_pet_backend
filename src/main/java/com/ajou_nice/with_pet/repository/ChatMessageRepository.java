package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.ChatMessage;
import com.ajou_nice.with_pet.domain.entity.ChatRoom;
import com.ajou_nice.with_pet.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findAllByChatRoomOrderBySendTimeAsc(ChatRoom chatRoom);

	Optional<ChatMessage> findChatMessageByIdOrderBySendTime(Long chatRoomId);
}
