package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.NotificationResponse;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.NotificationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponse> getNotification(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        List<Notification> notifications = notificationRepository.findAllByReceiver(
                user.getUserId());
        return notifications.stream().map(NotificationResponse::of).collect(Collectors.toList());
    }
}
