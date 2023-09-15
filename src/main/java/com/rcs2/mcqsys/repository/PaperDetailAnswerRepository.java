package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.PaperDetailAnswer;

public interface PaperDetailAnswerRepository extends JpaRepository<PaperDetailAnswer, Long>{

	@Query(value="SELECT * FROM paper_detail_answer PD WHERE PD.paperid = ?1",nativeQuery = true)
	List<PaperDetailAnswer> listByPaperId(Long paperId);

}
