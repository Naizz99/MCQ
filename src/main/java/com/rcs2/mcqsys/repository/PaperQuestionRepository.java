package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.PaperQuestion;

@Repository("repo")
public interface PaperQuestionRepository extends JpaRepository<PaperQuestion, Long>{

	@Query(value="SELECT * FROM paper_question QS WHERE QS.paperId = ?1 ORDER BY question_id ASC",nativeQuery = true)
	List<PaperQuestion> findAllById(Long paperId);

	@Query(value="SELECT * FROM paper_question WHERE paper_question.paperid = ?1 ORDER BY pq_id DESC LIMIT 1;",nativeQuery = true)
	PaperQuestion getLastQuestionByPaper(Long paperID);

	@Query(value="SELECT DISTINCT(module_id) from paper_question WHERE paperid = ?1",nativeQuery = true)
	long[] listAllModulesByPaper(long paperID);

	@Query(value="SELECT * FROM paper_question WHERE paper_question.paperid = ?1 AND paper_question..module_id = ?2 ORDER BY pq_id ASC",nativeQuery = true)
	List<PaperQuestion> listAllByPaperAndModule(Long paperId, long moduleId);

	@Query(value="SELECT COUNT(pq_id) from paper_question WHERE paperid = ?1",nativeQuery = true)
	int getCountByPaper(Long paperId);
	


}
