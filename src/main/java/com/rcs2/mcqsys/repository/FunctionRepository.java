package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Functions;

public interface FunctionRepository extends JpaRepository<Functions, Long>{

	@Query(value="SELECT * FROM functions WHERE functions.is_parent = true AND functions.active = true",nativeQuery = true)
	List<Functions> listParentFunctions();

	@Query(value="SELECT * FROM functions WHERE functions.parent_id = ?1",nativeQuery = true)
	List<Functions> listByParent(Long parentId);

}
