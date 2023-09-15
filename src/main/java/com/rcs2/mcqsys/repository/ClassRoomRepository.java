package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoom;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long>{

	@Query(value="SELECT * FROM classroom WHERE classroom.lecturer_id = ?1",nativeQuery = true)
	List<ClassRoom> listAllByLecturer(Long userId);

	@Query(value="SELECT COUNT(classroom.classroom_id) FROM classroom WHERE classroom.status = true",nativeQuery = true)
	int getAciveCount();

	@Query(value="SELECT COUNT(classroom.classroom_id) FROM classroom",nativeQuery = true)
	int getAllCount();

	@Query(value="SELECT COUNT(classroom.classroom_id) FROM classroom WHERE classroom.lecturer_id = ?1",nativeQuery = true)
	int getCountByLecture(Long userId);

	@Query(value="SELECT COUNT(classroom.classroom_id) FROM classroom WHERE classroom.status = true AND classroom.lecturer_id = ?1",nativeQuery = true)
	int getActiveCountByLecture(Long userId);
	
	@Query(value="SELECT * FROM classroom JOIN classroom_student ON classroom.classroom_id = classroom_student.class_room_id JOIN student ON student.student_id = classroom_student.student_id JOIN user ON user.user_id = student.user_id JOIN student_parent ON user.user_id = student_parent.student_id WHERE student_parent.parent_id=?1",nativeQuery = true)
	List<ClassRoom> listAllByParent(Long userId);

	@Query(value="SELECT * FROM classroom WHERE classroom.login_id = ?1",nativeQuery = true)
	ClassRoom getByLogginId(String logginId);
}


