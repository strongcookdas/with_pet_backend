package com.ajou_nice.with_pet.group.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.group.model.dto.PartyAddPartyByIsbnResponse;
import com.ajou_nice.with_pet.group.model.dto.PartyAddPartyByIsbnRequest;
import com.ajou_nice.with_pet.group.model.dto.add.PartyAddRequest;
import com.ajou_nice.with_pet.group.model.dto.add.PartyAddResponse;
import com.ajou_nice.with_pet.group.model.dto.get.PartyGetInfosResponse;
import com.ajou_nice.with_pet.group.service.PartyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/parties")
@Slf4j
@Api(tags = "Party API")
public class PartyController {

    private final PartyService partyService;

    @PostMapping("/members")
    @ApiOperation(value = "그룹 식별자로 기존 그룹 추가")
    public Response<PartyAddPartyByIsbnResponse> addPartyByPartyIsbn(@ApiIgnore Authentication authentication, @RequestBody PartyAddPartyByIsbnRequest partyAddPartyByIsbnRequest) {
        PartyAddPartyByIsbnResponse partyAddPartyByIsbnResponse = partyService.addPartyByPartyIsbn(authentication.getName(), partyAddPartyByIsbnRequest);
        return Response.success(partyAddPartyByIsbnResponse);
    }

    @GetMapping
    @ApiOperation(value = "그룹 상세 리스트 조회")
    public Response<List<PartyGetInfosResponse>> getPartyInfoList(@ApiIgnore Authentication authentication) {
        List<PartyGetInfosResponse> partyGetInfosResponse = partyService.getPartyInfoList(authentication.getName());
        return Response.success(partyGetInfosResponse);
    }

    @PostMapping
    @ApiOperation("그룹 생성")
    public Response<PartyAddResponse> createParty(@ApiIgnore Authentication authentication, @RequestBody PartyAddRequest partyAddRequest) {
        PartyAddResponse partyAddResponse = partyService.createParty(authentication.getName(), partyAddRequest);
        return Response.success(partyAddResponse);
    }

    @DeleteMapping("/{partyId}")
    @ApiOperation(value = "그룹 탈퇴")
    public Response<Void> leaveParty(@ApiIgnore Authentication authentication, @PathVariable Long partyId) {
        return Response.success(partyService.leaveParty(authentication.getName(), partyId));
    }

    @DeleteMapping("/{partyId}/members/{memberId}")
    @ApiOperation(value = "그룹 멤버 방출")
    public Response<Void> expelMember(@ApiIgnore Authentication authentication, @PathVariable Long partyId, @PathVariable Long memberId) {
        return Response.success(partyService.expelMember(authentication.getName(), partyId, memberId));
    }


}
