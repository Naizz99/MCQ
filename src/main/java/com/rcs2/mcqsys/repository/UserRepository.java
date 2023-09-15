package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.rcs2.mcqsys.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
	
	@Query(value="SELECT * FROM user WHERE user.email = ?1 LIMIT 1",nativeQuery = true)
	User getByEmail(String emailAddress);
	
	@Query(value="SELECT * FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE user.mobile = ?1 AND role.role = 'STUDENT'",nativeQuery = true)
	List<User> getStudentsByMobile(String mobile);
	
	@Query(value="SELECT * FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE user.email = ?1 AND role.role = 'STUDENT'",nativeQuery = true)
	User getStudentsByEmail(String studentEmail);
	
	@Query(value="SELECT * FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE LOWER(user.email) LIKE %?1% AND role.role = 'STUDENT'",nativeQuery = true)
	List<User> getStudentListByEmail(String email);

	@Query(value="SELECT COUNT(user.user_id) FROM user WHERE user.email = ?1",nativeQuery = true)
	int getCountByEmail(String prtEmail);
	
	@Query(value="SELECT COUNT(user.user_id) FROM user WHERE user.mobile = ?1",nativeQuery = true)
	int getCountByMobile(String mobile);
	
	@Query(value="SELECT * FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE role.role = 'PARENT'",nativeQuery = true)
	List<User> getAllParents();

	@Query(value="SELECT student_parent.parent_id FROM student_parent JOIN user ON student_parent.student_id = user.user_id WHERE user.email = ?1 LIMIT 1",nativeQuery = true)
	Long getParentByStudent(String studentEmail);

	@Query(value="SELECT COUNT(student_parent.parent_id) FROM student_parent JOIN user ON student_parent.student_id = user.user_id WHERE user.email = ?1",nativeQuery = true)
	int getParentCountByStudent(String studentEmail);

	@Query(value="SELECT COUNT(user.user_id) FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE user.email = ?1 AND role.role = 'PARENT'",nativeQuery = true)
	int getParentCountByEmail(String prtEmail);

	@Query(value="SELECT * FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE user.email = ?1 AND role.role = 'PARENT'",nativeQuery = true)
	User getParentByEmail(String prtEmail);

	@Query(value="SELECT COUNT(user.user_id) FROM user WHERE user.username = ?1",nativeQuery = true)
	int getCountByUserName(String username);

	@Query(value="SELECT COUNT(user.user_id) FROM user WHERE user.user_id = ?1",nativeQuery = true)
	int getCountByUserId(Long userId);

	@Query(value="SELECT * FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE role.role = ?1",nativeQuery = true)
	List<User> findAllByRole(String role);

	@Query(value="SELECT COUNT(name) FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE role.role = ?1",nativeQuery = true)
	int getCountByRole(String role);

	@Query(value="SELECT COUNT(user.user_id) FROM user JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE user.email = ?1 AND role.role = 'STUDENT'",nativeQuery = true)
	int getStudentCountByEmail(String email);

}




