package com.ajou_nice.with_pet.withpet_service.controller;


import com.ajou_nice.with_pet.withpet_service.model.dto.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.withpet_service.service.WithPetServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/services")
@Api(tags = "WithPetService API")
public class WithPetServiceController {

	private final WithPetServiceService withPetServiceService;

	@GetMapping
	@ApiOperation(value = "위드펫 서비스 리스트 조회")
	public Response<List<WithPetServiceResponse>> showWithPetServices(){
		List<WithPetServiceResponse> withPetServiceList = withPetServiceService.showWithPetServices();
		return Response.success(withPetServiceList);
	}
}
