package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.enums.NotificationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @NotNull
    private String content;

    @NotNull
    private String url;

    @NotNull
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User receiver;


    public static Notification of(String content, String url, NotificationType notificationType,
            User user) {
        return Notification.builder()
                .content(content)
                .url(url)
                .isRead(false)
                .notificationType(notificationType)
                .receiver(user)
                .build();
    }

    public void updateIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
