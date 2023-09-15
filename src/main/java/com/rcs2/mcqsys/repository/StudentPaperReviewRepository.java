package com.rcs2.mcqsys.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.StudentPaperReview;

public interface StudentPaperReviewRepository extends JpaRepository<StudentPaperReview, Long>{

	@Query(value="SELECT COUNT(student_paper_review.spr_id) FROM student_paper_review",nativeQuery = true)
	int getReviewCount();

	@Query(value="SELECT COUNT(student_paper_review.spr_id) FROM student_paper_review JOIN student ON student_paper_review.student_id = student.student_id JOIN student_parent ON student.user_id = student_parent.student_id WHERE student_parent.parent_id=?1",nativeQuery = true)
	int getCompleteCount(Long userId);

	@Query(value="SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(active_sessions.end_time, active_sessions.start_time)))) AS totalhour FROM active_sessions JOIN student ON active_sessions.student_id = student.student_id JOIN student_parent ON student.user_id = student_parent.student_id WHERE student_parent.parent_id=?1",nativeQuery = true)
	LocalTime getTotalTime(Long userId);

	@Query(value="SELECT * FROM student_paper_review JOIN student ON student_paper_review.student_id = student.student_id JOIN student_parent ON student.user_id = student_parent.student_id WHERE student_parent.parent_id=?1 ORDER BY student_paper_review.result DESC LIMIT 1",nativeQuery = true)
	StudentPaperReview getHighestMark(Long userId);

	@Query(value="SELECT * FROM student_paper_review WHERE student_paper_review.student_id = ?1 ORDER BY seated_date DESC",nativeQuery = true)
	List<StudentPaperReview> listByStudent(Long userId);

	@Query(value="SELECT * FROM student_paper_review WHERE student_paper_review.paperid = ?1",nativeQuery = true)
	List<StudentPaperReview> listByPaperId(Long paperId);

	@Query(value="SELECT * FROM student_paper_review JOIN paper ON student_paper_review.paperid = paper.paper_id WHERE paper.subject_id=?1 AND student_paper_review.student_id = ?2  ORDER BY seated_date DESC",nativeQuery = true)
	List<StudentPaperReview> getResultsBySubjectAndStudent(Long subjectId, Long studentId);

	@Query(value="SELECT COUNT(student_paper_review.spr_id) FROM student_paper_review JOIN paper ON student_paper_review.paperid = paper.paper_id WHERE paper.subject_id = ?1 AND student_paper_review.student_id = ?2",nativeQuery = true)
	int getCompleteCountBySubjectAndStudent(Long subjectId, Long studentId);

	@Query(value="SELECT COUNT(student_paper_review.spr_id) FROM student_paper_review WHERE student_paper_review.student_id = ?1",nativeQuery = true)
	int getCountByStudent(Long studentId);

	@Query(value="SELECT COUNT(student_paper_review.spr_id) FROM student_paper_review WHERE student_paper_review.paperid = ?1",nativeQuery = true)
	int getTotalAttendCount(Long paperId);
	
	@Query(value="SELECT COUNT(student_paper_review.spr_id) FROM student_paper_review JOIN paper ON student_paper_review.paperid = paper.paper_id WHERE paper.create_by = ?1",nativeQuery = true)
	int getTotalAttendCountByUser(String username);

	@Query(value="SELECT AVG(result) FROM student_paper_review WHERE student_paper_review.paperid= ?1",nativeQuery = true)
	double getAverageScoreByPaperId(Long paperId);

}

