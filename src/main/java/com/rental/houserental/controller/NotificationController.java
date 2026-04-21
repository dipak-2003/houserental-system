package com.rental.houserental.controller;

import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.entity.Notification;
import com.rental.houserental.service.CustomUserDetails;
import com.rental.houserental.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {

    private final CustomUserDetails userDetailsService;
    private final NotificationService notificationService;

    // Get all notifications by userId
    @GetMapping("/user-notice")
    public ResponseEntity<?> getUserNotifications(@RequestHeader("Authorization") String authHeader) throws Exception {

        LoggedUser loggedUser=userDetailsService.loadUserByToken(authHeader);
        try {
            List<Notification> notifications = notificationService.getUserNotifications(loggedUser.getId(),loggedUser.getRole());

            if (notifications.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body("No notifications found");
            }

            return ResponseEntity.ok(notifications);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching notifications: " + e.getMessage());
        }
    }

    //  Mark notification as read
    @PutMapping("/read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        try {
            notificationService.markAsRead(id);
            return ResponseEntity.ok("Notification marked as read");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating notification: " + e.getMessage());
        }
    }

    // Create notification

    //  Unread count (userId + role)
    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(@RequestHeader("Authorization") String authHeader) throws Exception {

        LoggedUser loggedUser=userDetailsService.loadUserByToken(authHeader);
        try {
            int count = notificationService.getUnreadCount(loggedUser.getId(), loggedUser.getRole());
            return ResponseEntity.ok(count);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching unread count: " + e.getMessage());
        }
    }
}