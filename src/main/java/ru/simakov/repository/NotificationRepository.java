package ru.simakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.simakov.model.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
