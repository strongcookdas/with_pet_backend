package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.NotificationResponse;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.EmitterRepository;
import com.ajou_nice.with_pet.repository.NotificationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private static final Long TIMEOUT = 60 * 1000 * 60L;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<NotificationResponse> getNotification(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        List<Notification> notifications = notificationRepository.findAllByReceiver(
                user.getUserId());
        return notifications.stream().map(NotificationResponse::of).collect(Collectors.toList());
    }

    public SseEmitter subscribe(String userId, String lastEventId) throws IOException {
        String emitterId = makeTimeIncludeId(userId);
        log.info("emitterId = {}", emitterId);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(TIMEOUT));

        //시간 만료일 경우 자동으로 리포지토리에서 삭제 처리 콜백
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));

        //503 에러 방지 더미데이터
        emitter.send(SseEmitter.event().name("connect"));

        return emitter;
    }

    private String makeTimeIncludeId(String userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId,
            NotificationResponse notificationResponse) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(notificationResponse, MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            emitterRepository.deleteById(eventId);
            log.error("SSE Connect Error", e);
        }
    }

    @Transactional
    public void send(Notification notification) {
        notificationRepository.save(notification);
        log.info("==== 알림 저장 =====");

        String receiverId = String.valueOf(notification.getReceiver().getId());

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(
                receiverId);

        emitters.forEach(
                (key, emitter) -> {
                    sendNotification(emitter, receiverId, NotificationResponse.of(notification));
                }
        );
        log.info("==== SSE 처리 완료 ====");
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmail(Notification notification) {
        emailService.sendEmail(notification.getReceiver().getEmail(),
                "위드펫" + notification.getNotificationType().name() + "알림",
                notification.getContent());
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmails(List<Notification> notifications) {
        notifications.forEach(n -> {
            emailService.sendEmail(n.getReceiver().getEmail(),
                    "위드펫" + n.getNotificationType().name() + "알림",
                    n.getContent());
        });
    }


}
