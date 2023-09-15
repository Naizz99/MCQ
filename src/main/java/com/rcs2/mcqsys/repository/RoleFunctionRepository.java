package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.RoleFunction;

public interface RoleFunctionRepository extends JpaRepository<RoleFunction, Long>{

	@Query(value="SELECT * FROM role_function WHERE role_function.role = ?1",nativeQuery = true)
	List<RoleFunction> listAllByRole(Long roleId);

	@Query(value="SELECT * FROM role_function WHERE role_function.role = ?1 AND role_function.function_id = ?2",nativeQuery = true)
	RoleFunction getByRoleAndFunction(Long roleId, Long functionId);

	@Query(value="SELECT * FROM role_function WHERE role_function.function_id = ?1 LIMIT 1",nativeQuery = true)
	RoleFunction getByFunction(Long functionId);
	
	@Query(value="SELECT COUNT(role_function.ufl_id) FROM role_function WHERE role_function.role = ?1 AND role_function.function_id = ?2",nativeQuery = true)
	int getCountByRoleAndFunction(Long roleId, Long functionId);
	
	@Query(value="SELECT DISTINCT role_function.function_id FROM role_function JOIN role ON role_function.role = role.role_id JOIN user_role ON  role.role_id = user_role.role_id JOIN user ON  user_role.user_id = user.user_id WHERE user.user_id = ?1",nativeQuery = true)
	List<Long> getFunctionsForUser(long userId);

}
