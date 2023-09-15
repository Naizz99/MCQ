package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoomPaperReviewDetail;

public interface ClassRoomPaperReviewDetailRepository extends JpaRepository<ClassRoomPaperReviewDetail, Long>{

	@Query(value="SELECT * FROM classroom_paper_review_detail WHERE classroom_paper_review_detail.paperid = ?1 ORDER BY question_id ASC;",nativeQuery = true)
	List<ClassRoomPaperReviewDetail> listAllByPaperId(Long paperId);

	@Query(value="SELECT * FROM classroom_paper_review_detail WHERE classroom_paper_review_detail.review_id = ?1 ORDER BY question_id ASC;",nativeQuery = true)
	List<ClassRoomPaperReviewDetail> listAllByReviewId(Long reviewId);

	@Query(value="SELECT * FROM classroom_paper_review_detail WHERE classroom_paper_review_detail.review_id = ?1 AND classroom_paper_review_detail.module_id = ?2 ORDER BY question_id ASC",nativeQuery = true)
	List<ClassRoomPaperReviewDetail> listAllByReviewAndModule(Long reviewId, long moduleId);
	
	@Query(value="SELECT * FROM classroom_paper_review_detail WHERE classroom_paper_review_detail.student_id = ?1 AND classroom_paper_review_detail.module_id = ?2",nativeQuery = true)
	List<ClassRoomPaperReviewDetail> listAllByStudentAndModule(Long studentId, Long moduleId);

	@Query(value="SELECT COUNT(cprd_id) FROM classroom_paper_review_detail WHERE classroom_paper_review_detail.review_id = ?1",nativeQuery = true)
	int getCountByReview(Long cprId);

}

