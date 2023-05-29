package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.service.AwsS3Service;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@ApiOperation("image api")
@RequestMapping("/api/v1/file")
public class AmazonS3Controller {

	private final AwsS3Service awsS3Service;

	@PostMapping("/upload")
	public Response<List<String>> uploadFile(List<MultipartFile> multipartFiles){

		log.info("===================multipart file Request : {} ===================", multipartFiles);
		List<String> uploadedUrls = awsS3Service.uploadFile(multipartFiles);

		log.info("===================upload url response : {} ===================", uploadedUrls);
		return Response.success(uploadedUrls);
	}
}
