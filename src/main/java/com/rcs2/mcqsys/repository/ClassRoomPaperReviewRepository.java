package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoomPaperReview;

public interface ClassRoomPaperReviewRepository extends JpaRepository<ClassRoomPaperReview, Long>{

	@Query(value="SELECT COUNT(paperid) FROM classroom_paper_review WHERE classroom_paper_review.paperid = ?1 AND classroom_paper_review.class_room_id = ?2",nativeQuery = true)
	int getTotalAttemptsByClassRoom(Long paperId, Long classroomId);

	@Query(value="SELECT COUNT(paperid) FROM classroom_paper_review WHERE classroom_paper_review.student_email = ?1",nativeQuery = true)
	int getTotalAttempsByStudent(String studentEmail);

	@Query(value="SELECT SUM(result) FROM classroom_paper_review WHERE classroom_paper_review.student_email = ?1 AND classroom_paper_review.result IS NOT NULL",nativeQuery = true)
	double getTotalMarks(String studentEmail);

	@Query(value="SELECT * FROM classroom_paper_review WHERE classroom_paper_review.class_room_id = ?1",nativeQuery = true)
	List<ClassRoomPaperReview> listAllByClassRoom(Long classroomId);

	@Query(value="SELECT * FROM classroom_paper_review WHERE classroom_paper_review.student_id = ?1",nativeQuery = true)
	List<ClassRoomPaperReview> listByStudent(Long studentId);

	@Query(value="SELECT * FROM classroom_paper_review WHERE classroom_paper_review.class_room_id = ?1 AND classroom_paper_review.student_id = ?2",nativeQuery = true)
	List<ClassRoomPaperReview> listByStudentAndClassroom(long classroomId, Long studentId);

}
