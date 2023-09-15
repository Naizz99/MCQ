package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	@Query(value="SELECT * FROM role WHERE role.role = ?1",nativeQuery = true)
	Role getByRole(String string);

	@Query(value="SELECT * FROM role JOIN user_role ON role.role_id = user_role.role_id WHERE user_role.user_id = ?1",nativeQuery = true)
	List<Role> listByUser(Long userId);

	@Query(value="SELECT role.role FROM role JOIN user_role ON role.role_id = user_role.role_id WHERE user_role.user_id = ?1",nativeQuery = true)
	List<String> listRoleNamesByUser(Long userId);

	@Query(value="SELECT * FROM role WHERE role.is_public = true",nativeQuery = true)
	List<Role> listPublicRoles();
}
