package com.ajou_nice.with_pet.domain.dto.chat;


import java.time.LocalDateTime;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatMessageRequest {

	@Lob
	private String message;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime sendTime;

	private String receiverId;
}