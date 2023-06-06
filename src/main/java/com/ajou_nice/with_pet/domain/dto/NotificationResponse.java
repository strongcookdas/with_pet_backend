package com.ajou_nice.with_pet.domain.dto;

import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.enums.NotificationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationResponse {

    private Long notificationId;
    private String content;
    private String url;
    private Boolean isRead;
    private NotificationType notificationType;
    private LocalDateTime createdAt;


    public static NotificationResponse of(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .content(notification.getContent())
                .url(notification.getUrl())
                .isRead(notification.getIsRead())
                .notificationType(notification.getNotificationType())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
