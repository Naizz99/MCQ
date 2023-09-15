package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.Subject;

public interface PaperRepository extends JpaRepository<Paper, Long>{

	@Query(value="SELECT * FROM paper WHERE paper.active = true",nativeQuery = true)
	List<Paper> findAllActive();

	@Query(value="SELECT * FROM paper WHERE paper.publisher = ?1",nativeQuery = true)
	List<Paper> listAllByPublisher(Publisher poId);

	@Query(value="SELECT * FROM paper WHERE paper.grade = ?1 AND paper.subject_id = ?2",nativeQuery = true)
	List<Paper> studentListPublishersByGrade(Grade grade,Subject subject);
	
	@Query(value="SELECT * FROM paper WHERE paper.grade = ?1 AND paper.subject_id = ?2 AND paper.publisher = ?3 AND paper.active=true",nativeQuery = true)
	List<Paper> studentListPapersByPublisher(Grade grade,Long subjectId, Long poId);

	@Query(value="SELECT * FROM paper WHERE paper.active = true",nativeQuery = true)
	List<Paper> listAllByStatus();

	@Query(value="SELECT COUNT(paperid) FROM student_paper_review WHERE student_paper_review.paperid = ?1",nativeQuery = true)
	int getTotalAttempts(long paperId);
	
	@Query(value="SELECT COUNT(paper_id) FROM paper",nativeQuery = true)
	int getPaperCount();

	@Query(value="SELECT * FROM paper WHERE paper.active = true AND paper.publisher <> 0",nativeQuery = true)
	List<Paper> listAllForLecturer();

	@Query(value="SELECT * FROM paper WHERE paper.create_by = ?1",nativeQuery = true)
	List<Paper> listAllByUsername(String username);

	@Query(value="SELECT * FROM paper WHERE paper.publisher = ?2 AND paper.create_by = ?1",nativeQuery = true)
	List<Paper> listAllByLecturer(String username, Publisher publisher);

	@Query(value="SELECT * FROM paper WHERE paper.available_for_buy = true AND paper.active=true AND create_by != ?1",nativeQuery = true)
	List<Paper> listMarketAvailablePapers(String username);

	@Query(value="SELECT COUNT(paper_id) FROM paper WHERE paper.active=true",nativeQuery = true)
	Object getActivePapers();
	
	@Query(value="SELECT COUNT(paper_id) FROM paper WHERE paper.active=false",nativeQuery = true)
	Object getDeactivePapers();

	@Query(value="SELECT * FROM paper WHERE paper.publisher = ?1 GROUP BY create_by",nativeQuery = true)
	List<Paper> listAllByPublisherGroupByCreateUser(Publisher poId);

	@Query(value="SELECT * FROM paper GROUP BY create_by",nativeQuery = true)
	List<Paper> listAllGroupByCreateUser();

	@Query(value="SELECT COUNT(paper_id) FROM paper WHERE paper.create_by=?1",nativeQuery = true)
	int getPaperCountByUser(String createBy);

	@Query(value="SELECT COUNT(paper_id) FROM paper WHERE paper.create_by=?1 AND paper.active=true",nativeQuery = true)
	int getActivePaperCountByUser(String createBy);

	@Query(value="SELECT COUNT(paper_id) FROM paper WHERE paper.create_by=?1 AND paper.active=false",nativeQuery = true)
	int getDeactivePaperCountByUser(String createBy);

}


