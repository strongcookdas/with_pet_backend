package com.ajou_nice.with_pet.domain.entity;


import com.ajou_nice.with_pet.domain.dto.chat.ChatMessageRequest;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ChatMessage {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;


	//메시지 대신 사진이 올 수 도 있기 때문에
	@Lob
	private String message;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roomId")
	private ChatRoom chatRoom;

	//메시지를 작성한 사람의 Id(user의) -> withPet3 처럼
	private String senderId;
	private LocalDateTime sendTime;

	public static ChatMessage toEntity(ChatMessageRequest chatMessageRequest,ChatRoom chatRoom, String userId){

		return ChatMessage.builder()
				.message(chatMessageRequest.getMessage())
				.chatRoom(chatRoom)
				.sendTime(chatMessageRequest.getSendTime())
				.senderId(userId)
				.build();
	}
}
