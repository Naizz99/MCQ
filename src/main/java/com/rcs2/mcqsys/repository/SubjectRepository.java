package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;

public interface SubjectRepository extends JpaRepository<Subject, Long>  {
	
	@Query(value="SELECT * FROM subject WHERE subject.grade_id = ?1",nativeQuery = true)
	public List<Subject> listAllSubjectByGrade(long gradeId);

	@Query(value="SELECT * FROM subject WHERE subject.active = true",nativeQuery = true)
	public List<Subject> findAllActive();

	@Query(value="SELECT * FROM subject WHERE subject.grade_id = ?1",nativeQuery = true)
	public List<Subject> studentListSubjectsByGrade(Grade grade);

	@Query(value="SELECT subject.* FROM subject JOIN paper ON subject.subject_id = paper.subject_id JOIN student_paper_review ON paper.paper_id = student_paper_review.paperid WHERE student_paper_review.student_id = ?1 GROUP BY subject.subject_id",nativeQuery = true)
	public List<Subject> listByStudentPaperReview(Long studentId);

	@Query(value="SELECT subject.* FROM subject JOIN paper ON subject.subject_id = paper.subject_id JOIN classroom_paper_review ON paper.paper_id = classroom_paper_review.paperid WHERE classroom_paper_review.student_id = ?1 GROUP BY subject.subject_id",nativeQuery = true)
	public List<Subject> listByClassRoomPaperReview(Long studentId);

	@Query(value="SELECT subject.* FROM subject JOIN paper ON subject.subject_id = paper.subject_id JOIN classroom_paper_review ON paper.paper_id = classroom_paper_review.paperid WHERE classroom_paper_review.class_room_id = ?1 AND classroom_paper_review.student_id = ?2 GROUP BY subject.subject_id",nativeQuery = true)
	public List<Subject> listByClassRoomPaperReview(Long classroomId, Long studentId);
	
	
}


