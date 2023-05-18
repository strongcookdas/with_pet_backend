package com.ajou_nice.with_pet.domain.dto.chat;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatRoomRequest {

	private String otherId;
	private LocalDateTime createTime;
}
