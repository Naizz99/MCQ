package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoomMessages;

public interface ClassRoomMessagesRepository extends JpaRepository<ClassRoomMessages, Long>{

	@Query(value="SELECT * FROM classroom_messages WHERE classroom_messages.sent_to = ?1",nativeQuery = true)
	List<ClassRoomMessages> listAllByReceiver(String email);

	@Query(value="SELECT * FROM classroom_messages WHERE classroom_messages.sent_to = ?1 AND classroom_messages.read_status = false",nativeQuery = true)
	List<ClassRoomMessages> listAllUnreadByReceiver(String email);

	@Query(value="SELECT COUNT(message) FROM classroom_messages WHERE classroom_messages.sent_to = ?1 AND classroom_messages.read_status = false",nativeQuery = true)
	int unreadMessageCountByReceiver(String email);

	@Query(value="SELECT * FROM classroom_messages WHERE classroom_messages.sent_to = ?1 AND classroom_messages.classroom_id = ?2",nativeQuery = true)
	List<ClassRoomMessages> allMessageByReceiver(String email, long classroomId);

	@Query(value="SELECT * FROM classroom_messages "
			+ "WHERE (classroom_messages.sent_from = ?1 OR classroom_messages.sent_from = ?2) "
			+ "AND (classroom_messages.sent_to = ?2 OR classroom_messages.sent_to = ?1) "
			+ "ORDER BY sent_date ASC,sent_time;",nativeQuery = true)
	List<ClassRoomMessages> listAll(String sentFrom, String sentTo);
	
	@Query(value="SELECT * FROM classroom_messages "
			+ "WHERE classroom_messages.classroom_id = ?2 "
			+ "AND (classroom_messages.sent_to = ?1 OR classroom_messages.sent_from = ?1) "
			+ "GROUP BY IF (classroom_messages.sent_to = ?1 , classroom_messages.sent_from,classroom_messages.sent_to) "
			+ "ORDER BY sent_date DESC,sent_time DESC",nativeQuery = true)
	List<ClassRoomMessages> allSenders(String email, long classroomId);

	@Query(value="SELECT message FROM classroom_messages "
			+ "WHERE classroom_messages.classroom_id = ?1 "
			+ "AND ((classroom_messages.sent_from = ?2 AND classroom_messages.sent_to = ?3) OR (classroom_messages.sent_from = ?3 AND classroom_messages.sent_to = ?2)) "
			+ "ORDER BY sent_date DESC,sent_time DESC LIMIT 1",nativeQuery = true)
	String getLastMessage(long classroomId, String email, String email2);

	@Query(value="SELECT * FROM classroom_messages WHERE classroom_messages.classroom_id = ?1",nativeQuery = true)
	List<ClassRoomMessages> listAllByClassRoom(Long classroomId);

}
