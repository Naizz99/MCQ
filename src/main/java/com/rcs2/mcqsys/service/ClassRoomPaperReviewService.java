package com.rcs2.mcqsys.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.ClassRoomPaperReview;
import com.rcs2.mcqsys.model.StudentPaperReview;
import com.rcs2.mcqsys.repository.ClassRoomPaperReviewRepository;
import com.rcs2.mcqsys.repository.StudentPaperReviewRepository;

@Service
@Transactional
public class ClassRoomPaperReviewService {
	@Autowired
	 private ClassRoomPaperReviewRepository repo;
	  
	 public List<ClassRoomPaperReview> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(ClassRoomPaperReview classRoomPaperReview) {
	     repo.save(classRoomPaperReview);
	 }
	  
	 public ClassRoomPaperReview get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public int getTotalAttemptsByClassRoom(Long paperId, Long classroomId) {
		return repo.getTotalAttemptsByClassRoom(paperId,classroomId);
	}

	public int getTotalAttempsByStudent(String studentEmail) {
		return repo.getTotalAttempsByStudent(studentEmail);
	}

	public double getTotalMarks(String studentEmail) {
		return repo.getTotalMarks(studentEmail);
	}

	public List<ClassRoomPaperReview> listAllByClassRoom(Long classroomId) {
		return repo.listAllByClassRoom(classroomId);
	}

	public List<ClassRoomPaperReview> listByStudent(Long studentId) {
		return repo.listByStudent(studentId);
	}

	public List<ClassRoomPaperReview> listByStudentAndClassroom(long classroomId, Long studentId) {
		return repo.listByStudentAndClassroom(classroomId,studentId);
	}

}
