package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.dog.DogInfoResponse;
import com.ajou_nice.with_pet.domain.dto.party.PartyMemberRequest;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.service.PartyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/groups")
@Slf4j
@Api(tags = "Party API")
public class PartyController {

    private final PartyService partyService;

    @PostMapping("/member")
    @ApiOperation(value = "그룹 멤버 추가")
    public Response<List<DogInfoResponse>> addMember(@ApiIgnore Authentication authentication,
            @RequestBody PartyMemberRequest partyMemberRequest) {
        List<DogInfoResponse> dogInfoResponses = partyService.addMember(authentication.getName(), partyMemberRequest);
        return Response.success(dogInfoResponses);
    }



}
