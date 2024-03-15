package com.ajou_nice.with_pet.repository;


import com.ajou_nice.with_pet.domain.entity.ChatRoom;
import com.ajou_nice.with_pet.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	@Query("select c from ChatRoom c where c.me.id=:userId")
	List<ChatRoom> findChatRoomByMyId(@Param("userId") Long userId);

	@Query("select c from ChatRoom c where c.other.id=:userId")
	List<ChatRoom> findChatRoomByOtherId(@Param("userId") Long userId);

	@Query("select c from ChatRoom c where c.me = :me and c.other = :other")
	Optional<ChatRoom> findChatRoomByMeAndOther(@Param("me")User me , @Param("other")User other);
}
