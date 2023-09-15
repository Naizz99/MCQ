package com.rcs2.mcqsys.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoomSessions;

public interface ClassRoomSessionsRepository extends JpaRepository<ClassRoomSessions, Long>{

	@Query(value="SELECT * FROM classroom_sessions WHERE classroom_sessions.classroom_id = ?1",nativeQuery = true)
	List<ClassRoomSessions> listAllByClassRoom(long classroomId);

	@Query(value="SELECT  SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(end_time, start_time)))) AS totalhours FROM classroom_sessions WHERE login_email = ?1",nativeQuery = true)
	LocalTime getTotalAttendTime(String studentEmail);
}
