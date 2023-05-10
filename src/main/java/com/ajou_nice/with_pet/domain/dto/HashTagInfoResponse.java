package com.ajou_nice.with_pet.domain.dto;

import com.ajou_nice.with_pet.domain.entity.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class HashTagInfoResponse {

	private Long hashTagId;
	private String hashTagName;

	public static HashTagInfoResponse of(HashTag hashTag){
		return HashTagInfoResponse.builder()
				.hashTagId(hashTag.getId())
				.hashTagName(hashTag.getName())
				.build();
	}
}
