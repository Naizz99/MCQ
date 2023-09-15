package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.PaperTemplate;

public interface PaperTemplateRepository extends JpaRepository<PaperTemplate, Long>{
	
	@Query(value="SELECT * FROM paper_template",nativeQuery = true)
	List<PaperTemplate> findAll();
	
}
