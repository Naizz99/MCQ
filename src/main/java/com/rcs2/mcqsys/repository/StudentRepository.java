package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.User;

public interface StudentRepository extends JpaRepository<Student, Long>{

	@Query(value="SELECT * FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE role.role = 'STUDENT'",nativeQuery = true)
	List<User> getAllStudent();

	@Query(value="SELECT * FROM student WHERE student.student_id = ?1",nativeQuery = true)
	Student getStudent(long id);

	@Query(value="SELECT COUNT(spr_id) FROM student_paper_review WHERE student_paper_review.student_id = ?1",nativeQuery = true)
	int countCompletedAttempts(Long studentId);

	@Query(value="SELECT * FROM student WHERE student.user_id = ?1",nativeQuery = true)
	Student getStudentByUserId(Long userId);

	@Query(value="SELECT * FROM student JOIN student_parent ON student.user_id = student_parent.student_id WHERE student_parent.parent_id = ?1",nativeQuery = true)
	List<Student> getStudentsByParent(Long parenId);

	@Query(value="SELECT DISTINCT student.student_id, student.attempts_allowed, student.gpa, student.grade, student.user_id FROM student "
			+ "JOIN classroom_student ON classroom_student.student_id = student.student_id "
			+ "JOIN classroom ON classroom.classroom_id = classroom_student.class_room_id "
			+ "WHERE classroom.lecturer_id = ?1",nativeQuery = true)
	List<Student> getStudentsByLecturer(Lecturer lecturerId);
	
}

