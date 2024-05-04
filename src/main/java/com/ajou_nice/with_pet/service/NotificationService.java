package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.NotificationResponse;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.NotificationType;
import com.ajou_nice.with_pet.repository.EmitterRepository;
import com.ajou_nice.with_pet.repository.NotificationRepository;
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
    private final EmailService emailService;

    private final ValidateCollection valid;

    /* 알림 목록 조회 */
    public List<NotificationResponse> getNotification(String userId) {
        User user = valid.userValidationById(userId);
        List<Notification> notifications = notificationRepository.findAllByReceiver(
                user.getId());
        return notifications.stream().map(NotificationResponse::of).collect(Collectors.toList());
    }

    /* SSE 구독 메서드 */
    public SseEmitter subscribe(String userId, String lastEventId) throws IOException {
        String emitterId = makeTimeIncludeId(userId);
        log.info("emitterId = {}", emitterId);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(TIMEOUT));

        //시간 만료일 경우 자동으로 리포지토리에서 삭제 처리 콜백
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));

        //503 에러 방지 더미데이터
        emitter.send(SseEmitter.event().name("connect").data("connect!"));
        log.info("=== sse 연결완료 ===");
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
            log.info("=== 알림 보내기 성공 ===");
        } catch (IOException e) {
            log.info("=== 알림 보내기 실패 ===");
            emitterRepository.deleteById(eventId);
            log.error("SSE Connect Error", e);
        }
    }

    @Transactional
    @Async("threadPoolTaskExecutor")
    public void send(Notification notification) {
        notificationRepository.save(notification);
        log.info("==== 알림 저장 =====");

        String receiverId = String.valueOf(notification.getReceiver().getId());

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(
                receiverId);

        emitters.forEach(
                (key, emitter) -> {
                    log.info("=== send emitters key : {} ===", key);
                    sendNotification(emitter, receiverId, NotificationResponse.of(notification));
                }
        );
        log.info("==== SSE 처리 완료 ====");
    }

    /* 이메일 알림 전송 메서드 */
    @Async("threadPoolTaskExecutor")
    public void sendEmail(Notification notification) {
        emailService.sendEmail(notification.getReceiver().getEmail(),
                "위드펫" + notification.getNotificationType().name() + "알림",
                String.format(notification.getContent() + "/n" + notification.getUrl()));
    }

    /* 여러개의 이메일 알림 전송 메서드 */
    @Async("threadPoolTaskExecutor")
    public void sendEmails(List<Notification> notifications) {
        notifications.forEach(n -> {
            emailService.sendEmail(n.getReceiver().getEmail(),
                    "위드펫" + n.getNotificationType().name() + "알림",
                    n.getContent());
        });
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void saveAllNotification(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    public Notification sendEmail(String content, String url, NotificationType notificationType,
            User user) {
        Notification notification = Notification.of(content, url, notificationType, user);
        emailService.sendHtmlEmail(notification.getReceiver().getEmail(),
                "[위드펫] " + notification.getNotificationType().name() + " 알림",
                "https://withpetoriginimage.s3.ap-northeast-1.amazonaws.com/4bf451c1-8062-4048-b279-319325c1fa6a.png",
                notification.getContent(),
                "http://ec2-13-125-242-183.ap-northeast-2.compute.amazonaws.com"
                        + notification.getUrl());
        return notification;

    }

}
