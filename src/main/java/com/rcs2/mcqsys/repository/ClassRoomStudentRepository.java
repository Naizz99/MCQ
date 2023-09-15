package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoomStudent;

public interface ClassRoomStudentRepository extends JpaRepository<ClassRoomStudent, Long>{

	@Query(value="SELECT * FROM classroom_student WHERE classroom_student.class_room_Id = ?1",nativeQuery = true)
	List<ClassRoomStudent> listAllByClassRoom(Long classroomId);

//	@Query(value="SELECT * FROM classroom_student WHERE classroom_student.student_email = ?1",nativeQuery = true)
//	List<ClassRoomStudent> listByMail(String email);

	@Query(value="SELECT * FROM classroom_student WHERE classroom_student.student_id = ?1",nativeQuery = true)
	List<ClassRoomStudent> listByUser(Long userId);
	
	@Query(value="SELECT * FROM classroom_student WHERE classroom_student.class_room_Id = ?1 AND classroom_student.status = true",nativeQuery = true)
	List<ClassRoomStudent> listAllOnline(Long classroomId);

//	@Query(value="SELECT * FROM classroom_student WHERE classroom_student.class_room_Id = ?1 AND classroom_student.student_email = ?2",nativeQuery = true)
//	ClassRoomStudent getByEmail(long classroomId, String studentEmail);

	@Query(value="SELECT * FROM classroom_student WHERE classroom_student.class_room_Id = ?1 AND classroom_student.student_id = ?2",nativeQuery = true)
	ClassRoomStudent getByStudentId(Long classroomId, Long studentId);
	
	@Query(value="SELECT COUNT(DISTINCT(student_id)) FROM classroom_student INNER JOIN classroom ON classroom_student.class_room_id = classroom.classroom_id WHERE classroom.lecturer_id = ?1",nativeQuery = true)
	int getCountByLecture(Long userId);

	@Query(value="SELECT classroom_student.class_room_student_id,classroom_student.active,classroom_student.create_by,classroom_student.create_date,classroom_student.update_by,classroom_student.update_date,classroom_student.class_room_id,classroom_student.student_id,classroom_student.`status` FROM classroom_student INNER JOIN classroom ON classroom_student.class_room_id = classroom.classroom_id WHERE classroom.lecturer_id = ?1",nativeQuery = true)
	List<ClassRoomStudent> listAllOnlineByLecturer(Long userId);

	@Query(value="SELECT COUNT(class_room_student_id) FROM classroom_student"
			+ " JOIN student ON classroom_student.student_id=student.student_id"
			+ " JOIN user ON student.user_id = user.user_id"
			+ " WHERE user.email = ?2 AND classroom_student.class_room_id=?1",nativeQuery = true)
	int listByUser(Long classroomId, String email);
}
