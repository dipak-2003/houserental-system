package com.rental.houserental.repository;

import com.rental.houserental.entity.Notification;
import com.rental.houserental.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUserIdAndRoleOrderByCreatedAtDesc(Long userId, Role role);
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.role = :role AND n.isRead = false")
    int countUnreadByUserIdAndRole(Long userId, Role role);

}
