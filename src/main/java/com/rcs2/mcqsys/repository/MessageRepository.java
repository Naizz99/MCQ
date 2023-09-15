package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
	
	@Query(value="SELECT * FROM message "
			+ "WHERE message.sent_to = ?1 OR message.sent_from = ?1 "
			+ "GROUP BY IF (message.sent_to = ?1 , message.sent_from,message.sent_to) "
			+ "ORDER BY sent_date DESC,sent_time DESC",nativeQuery = true)
	List<Message> allSenders(Long UserId);
	
	@Query(value="SELECT * FROM message "
			+ "WHERE (message.sent_from = ?1 OR message.sent_from = ?2) "
			+ "AND (message.sent_to = ?2 OR message.sent_to = ?1) "
			+ "ORDER BY sent_date ASC,sent_time;",nativeQuery = true)
	List<Message> listAll(Long sent_from,Long sent_to);

	@Query(value="SELECT message FROM message "
			+ "WHERE ((message.sent_from = ?1 AND message.sent_to = ?2) OR (message.sent_from = ?2 AND message.sent_to = ?1)) "
			+ "ORDER BY sent_date DESC,sent_time DESC LIMIT 1",nativeQuery = true)
	String getLastMessage(Long userId, Long userId2);

	@Query(value="SELECT COUNT(message) FROM message WHERE message.sent_to = ?1 AND message.read_status = false",nativeQuery = true)
	int unreadMessageCountByReceiver(Long userId);

	@Query(value="SELECT * FROM message WHERE message.sent_to = ?1 AND message.read_status = false",nativeQuery = true)
	List<Message> listAllUnread(Long userId);
}
