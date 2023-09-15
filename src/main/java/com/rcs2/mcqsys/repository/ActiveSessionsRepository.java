package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ActiveSessions;

public interface ActiveSessionsRepository extends JpaRepository<ActiveSessions, Long>{

	@Query(value="SELECT * FROM active_sessions WHERE active_sessions.student_id = ?1 AND active_sessions.active = true",nativeQuery = true)
	ActiveSessions getActiveSessionByStudent(Long studentId);

	@Query(value="SELECT COUNT(active_sessions.as_id) FROM active_sessions WHERE active_sessions.active = true",nativeQuery = true)
	int getActiveCount();

	@Query(value="SELECT * FROM active_sessions WHERE active_sessions.paperid = ?1",nativeQuery = true)
	List<ActiveSessions> listAllByPaperId(Long paperId);

}
