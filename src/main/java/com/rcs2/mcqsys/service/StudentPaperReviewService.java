package com.rcs2.mcqsys.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.StudentPaperReview;
import com.rcs2.mcqsys.repository.StudentPaperReviewRepository;

@Service
@Transactional
public class StudentPaperReviewService {
	@Autowired
	 private StudentPaperReviewRepository repo;
	  
	 public List<StudentPaperReview> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(StudentPaperReview studentPaperReview) {
	     repo.save(studentPaperReview);
	 }
	  
	 public StudentPaperReview get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public int getReviewCount() {
		return repo.getReviewCount();
	}

	public int getCompleteCount(Long userId) {
		return repo.getCompleteCount(userId);
	}

	public LocalTime getTotalTime(Long userId) {
		return repo.getTotalTime(userId);
	}

	public StudentPaperReview getHighestMark(Long userId) {
		return repo.getHighestMark(userId);
	}

	public List<StudentPaperReview> listByStudent(Long userId) {
		return repo.listByStudent(userId);
	}

	public List<StudentPaperReview> listByPaperId(Long paperId) {
		return repo.listByPaperId(paperId);
	}

	public List<StudentPaperReview> getResultsBySubjectAndStudent(Long subjectId, Long studentId) {
		return repo.getResultsBySubjectAndStudent(subjectId,studentId);
	}

	public long getCompleteCountBySubjectAndStudent(Long subjectId, Long studentId) {
		return repo.getCompleteCountBySubjectAndStudent(subjectId,studentId);
	}

	public int getCountByStudent(Long studentId) {
		return repo.getCountByStudent(studentId);
	}

	public int getTotalAttendCount(Long paperId) {
		return repo.getTotalAttendCount(paperId);
	}
	
	public int getTotalAttendCountByUser(String username) {
		return repo.getTotalAttendCountByUser(username);
	}

	public double getAverageScoreByPaperId(Long paperId) {
		try {
			return repo.getAverageScoreByPaperId(paperId);
		}catch(Exception ex){
			return 0.0;
		}
		
	}
}
