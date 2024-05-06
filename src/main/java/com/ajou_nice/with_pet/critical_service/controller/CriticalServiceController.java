package com.ajou_nice.with_pet.critical_service.controller;


import com.ajou_nice.with_pet.critical_service.model.dto.CriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.service.CriticalServiceService;
import com.ajou_nice.with_pet.domain.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/critical-services")
@Api(tags = "CriticalService API")
public class CriticalServiceController {

	private final CriticalServiceService criticalServiceService;

	@GetMapping
	@ApiOperation(value = "필수 위드펫 서비스 리스트 조회")
	public Response<List<CriticalServiceResponse>> showCriticalServices(){
		List<CriticalServiceResponse> criticalServiceResponseList = criticalServiceService.showCriticalServices();
		return Response.success(criticalServiceResponseList);
	}
}
