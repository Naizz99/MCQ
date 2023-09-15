package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Grade;
 
public interface GradeRepository extends JpaRepository<Grade, Long> {

	@Query(value="SELECT * FROM grade WHERE grade.active = true",nativeQuery = true)
	List<Grade> findAllActive();

	@Query(value="SELECT * FROM grade WHERE grade.grade = ?1",nativeQuery = true)
	Grade getByGrade(int grade);
 
}