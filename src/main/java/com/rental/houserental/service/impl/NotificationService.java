package com.rental.houserental.service.impl;

import com.rental.houserental.entity.Notification;
import com.rental.houserental.enums.Role;
import com.rental.houserental.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // get all notifications of user
    public List<Notification> getUserNotifications(Long userId, Role role) {
        return notificationRepository.findByUserIdAndRoleOrderByCreatedAtDesc(userId,role);
    }

    // mark as read
    public void markAsRead(Long id) {
        Notification n = notificationRepository.findById(id).orElseThrow();
        n.setRead(true);
        notificationRepository.save(n);
    }

    // create notification
    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }
    public int getUnreadCount(Long id,Role role){
        int unreadNumber=notificationRepository.countUnreadByUserIdAndRole(id,role);
        return unreadNumber;
    }
}