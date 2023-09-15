package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

	@Query(value="SELECT * FROM user_role WHERE user_role.user_id = ?1",nativeQuery = true)
	List<UserRole> listByUser(Long adminId);

	@Query(value="SELECT COUNT(user_role.ur_id) FROM user_role WHERE user_role.user_id = ?1 AND user_role.role_id = ?2",nativeQuery = true)
	int getCountByUserAndRole(Long userId, Long roleId);

	@Query(value="SELECT COUNT(user_role.ur_id) FROM user_role WHERE user_role.ur_id = ?1",nativeQuery = true)
	int getCountByUserAndRole(Long uRoleId);

	@Query(value="SELECT * FROM user_role WHERE user_role.user_id = ?1 AND user_role.role_id = ?2",nativeQuery = true)
	UserRole getByUserAndRole(Long userId,Long roleId);

	@Query(value="SELECT COUNT(user_role.ur_id) FROM user_role WHERE user_role.user_id = ?1",nativeQuery = true)
	int getCountByUser(Long userId);

}
