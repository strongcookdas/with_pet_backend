package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterDetailInfoResponse.PetSitterModifyInfoResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterMainResponse;
import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterRequest.PetSitterModifyInfoRequest;
import com.ajou_nice.with_pet.service.PetSitterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "PetSitter api")
public class PetSitterController {

	private final PetSitterService petSitterService;

	// 펫시터에서 필요한 api //

	// 펫시터 상세정보 조회 api //
	@GetMapping("api/v1/petsitter/{petsitterId}")
	@ApiOperation(value = "사용자의 펫시터 상세 정보 조회")
	public Response<PetSitterDetailInfoResponse> showPetSitterInfo(@PathVariable("petsitterId") Long petSitterId){

		PetSitterDetailInfoResponse petSitterDetailInfoResponse = petSitterService.showPetSitterDetailInfo(petSitterId);

		return Response.success(petSitterDetailInfoResponse);
	}

	// 펫시터 현재 자신 정보 조회 //
	@GetMapping("/api/v1/petsitter/show-myinfo")
	@ApiOperation(value = "펫시터의 자신 정보 조회")
	public Response<PetSitterModifyInfoResponse> showPetSitterSelfInfo(@ApiIgnore Authentication authentication){

		PetSitterModifyInfoResponse modifyInfoResponse = petSitterService.showMyInfo(
				authentication.getName());

		log.info("=============== petsitter's info : {} ================", modifyInfoResponse);

		return Response.success(modifyInfoResponse);
	}

	// 펫시터 my Info 수정  //
	@PutMapping("/api/v1/petsitter/update-myinfo")
	@ApiOperation(value = "펫시터의 펫시터정보 수정")
	public Response<PetSitterModifyInfoResponse> modifyPetSitterInfo(@ApiIgnore Authentication authentication,
			@RequestBody @Valid PetSitterModifyInfoRequest petSitterModifyInfoRequest){
		log.info("=============== petSitter modify info request : {} ================", petSitterModifyInfoRequest);

		PetSitterModifyInfoResponse modifyInfoResponse = petSitterService.updateMyInfo(petSitterModifyInfoRequest,
				authentication.getName());

		log.info("=============== petSitter modify info response : {} ================", modifyInfoResponse);
		return Response.success(modifyInfoResponse);
	}

	// 메인페이지 펫시터 조회 //
	@GetMapping("/api/v1/show-petsitter")
	@ApiOperation(value = "메인페이지 펫시터들 조회")
	public Response<Page<PetSitterMainResponse>> showPetSitters(
			@ApiIgnore @PageableDefault(size = 20, sort = "createdAt", direction = Direction.ASC)
			Pageable pageable) {
		Page<PetSitterMainResponse> petSitterMainResponses = petSitterService.getPetSitters(
				pageable);

		return Response.success(petSitterMainResponses);
	}
}
