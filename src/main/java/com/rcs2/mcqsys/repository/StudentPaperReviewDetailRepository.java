package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.StudentPaperReviewDetail;

public interface StudentPaperReviewDetailRepository extends JpaRepository<StudentPaperReviewDetail, Long>{

	@Query(value="SELECT * FROM student_paper_review_detail WHERE student_paper_review_detail.paperid = ?1 ORDER BY question_id ASC;",nativeQuery = true)
	List<StudentPaperReviewDetail> listAllByPaperId(Long paperId);

	@Query(value="SELECT * FROM student_paper_review_detail WHERE student_paper_review_detail.review_id = ?1 ORDER BY question_id ASC;",nativeQuery = true)
	List<StudentPaperReviewDetail> listAllByReviewId(Long reviewId);

	@Query(value="SELECT * FROM student_paper_review_detail WHERE student_paper_review_detail.review_id = ?1 AND student_paper_review_detail.module_id = ?2 ORDER BY question_id ASC",nativeQuery = true)
	List<StudentPaperReviewDetail> listAllByReviewAndModule(Long reviewId, long moduleId);
	
	@Query(value="SELECT * FROM student_paper_review_detail WHERE student_paper_review_detail.student_id = ?1 AND student_paper_review_detail.module_id = ?2",nativeQuery = true)
	List<StudentPaperReviewDetail> listAllByStudentAndModule(Long studentId, Long moduleId);

	@Query(value="SELECT COUNT(sprd_id) FROM student_paper_review_detail WHERE student_paper_review_detail.review_id = ?1",nativeQuery = true)
	int getCountByReview(Long sprId);

	@Query(value="SELECT * FROM student_paper_review_detail WHERE student_paper_review_detail.student_id = ?1",nativeQuery = true)
	List<StudentPaperReviewDetail> listAllByStudent(Long studentId);

	@Query(value="SELECT COUNT(sprd_id) FROM student_paper_review_detail WHERE student_paper_review_detail.student_id = ?1",nativeQuery = true)
	int getCountByStudent(Long studentId);

	@Query(value="SELECT COUNT(sprd_id) FROM student_paper_review_detail WHERE student_paper_review_detail.student_id = ?1 AND given_answer_pa_id <> ''",nativeQuery = true)
	int getAttendedCountByStudent(Long studentId);

	@Query(value="SELECT COUNT(sprd_id) FROM student_paper_review_detail WHERE student_paper_review_detail.student_id = ?1 AND given_answer_pa_id <> '' AND given_answer_pa_id = correct_answer_pa_id",nativeQuery = true)
	int getCorrectCountByStudent(Long studentId);

	@Query(value="SELECT COUNT(sprd_id) FROM student_paper_review_detail WHERE student_paper_review_detail.review_id = ?1 AND given_answer_pa_id <> ''",nativeQuery = true)
	int getAttendedCountByReview(long reviewId);
	
	@Query(value="SELECT COUNT(sprd_id) FROM student_paper_review_detail WHERE student_paper_review_detail.review_id = ?1 AND given_answer_pa_id <> '' AND given_answer_pa_id = correct_answer_pa_id",nativeQuery = true)
	int getCorrectCountByReview(long reviewId);

	@Query(value="SELECT COUNT(sprd_id) FROM student_paper_review_detail WHERE student_paper_review_detail.review_id = ?1",nativeQuery = true)
	int getQuestionCountByReview(Long reviewId);

}

