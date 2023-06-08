package com.ajou_nice.with_pet.domain.dto.review;
import com.ajou_nice.with_pet.domain.entity.Review;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReviewResponse {

	private Long reviewId;
	private double grade;

	private String reviewerImg;
	private String reviewerName;
	@Lob
	private String content;
	private LocalDateTime createdAt;


	public static List<ReviewResponse> toList(List<Review> reviews){
		return reviews.stream().map(review -> ReviewResponse.builder()
				.reviewId(review.getReviewId())
				.grade(review.getGrade())
				.reviewerImg(review.getReservation().getUser().getProfileImg())
				.reviewerName(review.getReservation().getUser().getName())
				.content(review.getContent())
				.createdAt(review.getCreatedAt())
				.build()).collect(Collectors.toList());
	}
}
