package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.NotificationResponse;
import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notifications")
@Api(tags = "Notification API")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @ApiIgnore
    public SseEmitter subscribe(Authentication authentication,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId)
            throws IOException {

        log.info("==== subscribe ====");

        if (authentication == null) {
            log.info("==== authentication = null ====");
            return null;
        }

        return notificationService.subscribe(authentication.getName(), lastEventId);
    }

    @GetMapping
    @ApiOperation(value = "알림 조회")
    public Response<List<NotificationResponse>> getNotifications(
            @ApiIgnore Authentication authentication) {
        return Response.success(notificationService.getNotification(authentication.getName()));
    }


}
