package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.StudentParent;
import com.rcs2.mcqsys.model.User;

public interface StudentParentRepository extends JpaRepository<StudentParent, Long>{

	@Query(value="SELECT COUNT(student_parent.student_parent_id) FROM student_parent WHERE student_parent.parent_id = ?1",nativeQuery = true)
	int getAllStudentsForParent(Long long1);

	@Query(value="SELECT * FROM student_parent WHERE student_parent.parent_id = ?1",nativeQuery = true)
	List<StudentParent> listAllByParent(Long userId);

	@Query(value="SELECT * FROM student_parent JOIN user ON user.user_id = student_parent.student_id WHERE user.email = ?1",nativeQuery = true)
	StudentParent getByStudentEmail(String studentEmail);

	@Query(value="SELECT * FROM student_parent JOIN user ON user.user_id = student_parent.student_id WHERE user.user_id = ?1",nativeQuery = true)
	StudentParent getByStudentUserId(Long userId);
	
	@Query(value="SELECT * FROM student_parent JOIN user ON student_parent.parent_id = user.user_id WHERE user.user_id = ?1",nativeQuery = true)
	List<StudentParent> getStudentsByParent(Long parenId);
	
	@Query(value="SELECT * FROM student_parent WHERE student_parent.student_id = ?1 LIMIT 1",nativeQuery = true)
	StudentParent getParentByStudent(Long userId);

	@Query(value="SELECT COUNT(student_parent.parent_id) FROM student_parent WHERE student_parent.student_id = ?1",nativeQuery = true)
	int getParentCountByStudent(Long userId);

	@Query(value="SELECT * FROM student_parent WHERE student_parent.student_id = ?1",nativeQuery = true)
	List<StudentParent> listAllByStudent(Long userId);

	@Query(value="SELECT COUNT(student_parent.parent_id) FROM student_parent WHERE student_parent.parent_id = ?1 AND student_parent.student_id = ?2",nativeQuery = true)
	int getCount(Long parentId, Long long1);

	@Query(value="SELECT * FROM student_parent WHERE student_parent.parent_id = ?1 AND student_parent.student_id = ?2",nativeQuery = true)
	StudentParent getByStudentAndParent(Long parentId, Long studentId);


}

