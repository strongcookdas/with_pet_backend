package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.petsitter.CreateServiceRequest;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterInfoResponse;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.service.AdminService;
import com.ajou_nice.with_pet.service.PetSitterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "PetSitter api")
public class PetSitterController {

	private final PetSitterService petSitterService;
	private final AdminService adminService;

	// 펫시터에서 필요한 api //

	// 1. 등록할 수 있는 위드펫 서비스 조회 //
	@GetMapping("/api/v1/petsitter/show-services")
	@ApiOperation(value = "위드펫 서비스 리스트 조회")
	public Response<List<WithPetServiceResponse>> showWithPetServices(){
		List<WithPetServiceResponse> withPetServiceList = adminService.showWithPetServices();

		log.info("=============== withPet service list : {} ================", withPetServiceList);

		return Response.success(withPetServiceList);
	}

	// 2. 서비스 선택 및 등록 //
	// 서비스 선택시 서비스 리스트로 들어온다 Request에 //
	@PostMapping("/api/v1/petsitter/services")
	@ApiOperation(value = "펫시터의 자신이 제공할 서비스 등록")
	public Response<PetSitterInfoResponse> updateWithPetServices(@ApiIgnore Authentication authentication,
			@RequestBody @Valid CreateServiceRequest createServiceRequest){
		log.info("=============== petSitter service list request : {} ================", createServiceRequest);

		PetSitterInfoResponse petSitterInfoResponse = petSitterService.updatePetSitterService(
				authentication.getName(), createServiceRequest);
		return Response.success(petSitterInfoResponse);
	}

	// 3. 메인페이지 //
	@GetMapping("/api/v1/show-petsitter")
	@ApiOperation(value = "메인페이지 펫시터들 조회")
	public


	// 3. 서비스 가격 수정 //

}
