package com.ajou_nice.with_pet.repository;

import com.ajou_nice.with_pet.domain.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select distinct n from Notification  n join fetch n.receiver where n.receiver.id =: userId order by n.createdAt desc ")
    List<Notification> findAllByReceiver(@Param("userId") Long userId);
}
