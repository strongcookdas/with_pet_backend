package com.ajou_nice.with_pet.repository;


import com.ajou_nice.with_pet.domain.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	@Query("select c from ChatRoom c where c.receiver.userId=:userId")
	List<ChatRoom> findChatRoomByUserId(@Param("userId") Long userId);
}
