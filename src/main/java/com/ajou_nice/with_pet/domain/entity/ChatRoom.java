package com.ajou_nice.with_pet.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class ChatRoom {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "my_userId")
	private User me;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "other_userId")
	private User other;

	@OneToMany(mappedBy = "chatRoom")
	private List<ChatMessage> roomMessages;

	private LocalDateTime myLastShowTime;
	private LocalDateTime lastModifiedTime;

	private int showMessageCount;

	private int allMessageCount;

	public void updateModifiedTime(LocalDateTime lastModifiedTime){
		this.lastModifiedTime = lastModifiedTime;
		myLastShowTime = lastModifiedTime;
		showMessageCount++;
		allMessageCount++;
	}

	public void updateMyLastShowTime(LocalDateTime lastShowTime){
		myLastShowTime = lastShowTime;
		showMessageCount++;
	}

	public static ChatRoom toEntity(User me, User other, LocalDateTime updateTime){
		return ChatRoom.builder()
				.me(me)
				.other(other)
				.myLastShowTime(updateTime)
				.lastModifiedTime(updateTime)
				.showMessageCount(0)
				.allMessageCount(0)
				.build();
	}
}
