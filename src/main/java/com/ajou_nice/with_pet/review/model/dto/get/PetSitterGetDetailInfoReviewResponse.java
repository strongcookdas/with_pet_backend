package com.ajou_nice.with_pet.review.model.dto.get;

import com.ajou_nice.with_pet.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PetSitterGetDetailInfoReviewResponse {
    private Long reviewId;
    private double reviewGrade;
    private String reviewerImg;
    private String reviewerName;
    @Lob
    private String reviewContent;
    private LocalDateTime reviewCreatedAt;

    public static List<PetSitterGetDetailInfoReviewResponse> toList(List<Review> reviews){
        return reviews.stream().map(review -> PetSitterGetDetailInfoReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reviewGrade(review.getGrade())
                .reviewerImg(review.getReservation().getUser().getProfileImg())
                .reviewerName(review.getReservation().getUser().getName())
                .reviewContent(review.getContent())
                .reviewCreatedAt(review.getCreatedAt())
                .build()).collect(Collectors.toList());
    }
}
