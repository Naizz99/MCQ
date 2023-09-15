package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Subject;
 
public interface ModuleRepository extends JpaRepository<Module, Long> {

	@Query(value="SELECT * FROM module WHERE module.active = true",nativeQuery = true)
	List<Module> findAllActive();

	@Query(value="SELECT * FROM module WHERE module.active = true AND module.subject_id = ?1",nativeQuery = true)
	List<Module> listAllActiveBySubject(Subject subject);

	@Query(value="SELECT * FROM module WHERE module.subject_id = ?1",nativeQuery = true)
	List<Module> listAllBySubject(Long subjectId);
 
}