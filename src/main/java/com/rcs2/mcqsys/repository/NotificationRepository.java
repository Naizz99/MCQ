package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

	@Query(value="SELECT * FROM notification WHERE notification.user_id = ?1",nativeQuery = true)
	List<Notification> listAll(Long userId);
	
	@Query(value="SELECT * FROM notification WHERE notification.user_id = ?1 AND notification.read_status = false",nativeQuery = true)
	List<Notification> listAllUnread(Long userId);

	@Query(value="SELECT COUNT(notification.ntf_id) FROM notification WHERE notification.read_status = false AND notification.user_id = ?1",nativeQuery = true)
	int unreadNotificationCountByReceiver(Long userId);

}
