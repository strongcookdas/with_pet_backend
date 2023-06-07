package com.ajou_nice.with_pet.domain.dto.chat;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatRoomRequest {

	private Long otherId;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime createTime;
}
