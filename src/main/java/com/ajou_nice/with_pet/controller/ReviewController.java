package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.review.ReviewRequest;
import com.ajou_nice.with_pet.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
@Api(tags = "Review API")
public class ReviewController {

	private final ReservationService reservationService;


	@PostMapping("/create-review")
	@ApiOperation(value = "유저의 리뷰 작성")
	public Response createReview(@ApiIgnore Authentication authentication,
			@RequestBody ReviewRequest reviewRequest){


		reservationService.postReview(authentication.getName(),reviewRequest);

		return Response.success("리뷰 작성이 완료되었습니다.");
	}
}
