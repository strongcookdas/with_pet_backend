package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.service.AwsS3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class AmazonS3Controller {

	private final AwsS3Service awsS3Service;

	@PostMapping("/upload")
	public Response<List<String>> uploadFile(List<MultipartFile> multipartFiles){

		List<String> uploadedUrls = awsS3Service.uploadFile(multipartFiles);
		return Response.success(uploadedUrls);
	}
}
