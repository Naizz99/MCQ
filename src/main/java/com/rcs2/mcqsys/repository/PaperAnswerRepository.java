package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.PaperAnswer;

public interface PaperAnswerRepository extends JpaRepository<PaperAnswer, Long>{
	
	@Query(value="SELECT * FROM paper_answer AN WHERE AN.pq_id = ?1",nativeQuery = true)
	List<PaperAnswer> listAllByQuestionId(Long pqId);

	@Query(value="SELECT * FROM paper_answer AN WHERE AN.paperid = ?1",nativeQuery = true)
	List<PaperAnswer> listAllByPaperId(Long paperId);

	@Query(value="SELECT * FROM paper_answer AN WHERE AN.pq_id = ?1 AND AN.answer_status = true",nativeQuery = true)
	PaperAnswer getCorrectAnswer(Long pqId);
	
}
