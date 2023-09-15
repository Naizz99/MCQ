package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rcs2.mcqsys.model.StudentPaperReviewDetail;
import com.rcs2.mcqsys.repository.StudentPaperReviewDetailRepository;

@Service
@Transactional
public class StudentPaperReviewDetailService {
	@Autowired
	 private StudentPaperReviewDetailRepository repo;
	  
	 public List<StudentPaperReviewDetail> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(StudentPaperReviewDetail reviewDetail) {
	     repo.save(reviewDetail);
	 }
	  
	 public StudentPaperReviewDetail get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<StudentPaperReviewDetail> listAllByPaperId(Long paperId) {
		return repo.listAllByPaperId(paperId);
	}

	public List<StudentPaperReviewDetail> listAllByReviewId(Long reviewId) {
		return repo.listAllByReviewId(reviewId);
	}

	public List<StudentPaperReviewDetail> listAllByReviewAndModule(Long reviewId, long moduleId) {
		return repo.listAllByReviewAndModule(reviewId,moduleId);
	}

	public List<StudentPaperReviewDetail> listAllByStudentAndModule(Long studentId, Long moduleId) {
		return repo.listAllByStudentAndModule(studentId,moduleId);
	}

	public int getCountByReview(Long sprId) {
		return repo.getCountByReview(sprId);
	}

	public List<StudentPaperReviewDetail> listAllByStudent(Long studentId) {
		return repo.listAllByStudent(studentId);
	}

	public int getCountByStudent(Long studentId) {
		return repo.getCountByStudent(studentId);
	}

	public int getAttendedCountByStudent(Long studentId) {
		return repo.getAttendedCountByStudent(studentId);
	}

	public int getCorrectCountByStudent(Long studentId) {
		return repo.getCorrectCountByStudent(studentId);
	}

	public int getAttendedCountByReview(long reviewId) {
		return repo.getAttendedCountByReview(reviewId);
	}
	
	public int getCorrectCountByReview(long reviewId) {
		return repo.getCorrectCountByReview(reviewId);
	}

	public int getQuestionCountByReview(Long reviewId) {
		return repo.getQuestionCountByReview(reviewId);
	}
}
