package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.ClassRoomPaperReviewDetail;
import com.rcs2.mcqsys.repository.ClassRoomPaperReviewDetailRepository;

@Service
@Transactional
public class ClassRoomPaperReviewDetailService {
	@Autowired
	 private ClassRoomPaperReviewDetailRepository repo;
	  
	 public List<ClassRoomPaperReviewDetail> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(ClassRoomPaperReviewDetail reviewDetail) {
	     repo.save(reviewDetail);
	 }
	  
	 public ClassRoomPaperReviewDetail get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<ClassRoomPaperReviewDetail> listAllByPaperId(Long paperId) {
		return repo.listAllByPaperId(paperId);
	}

	public List<ClassRoomPaperReviewDetail> listAllByReviewId(Long reviewId) {
		return repo.listAllByReviewId(reviewId);
	}

	public List<ClassRoomPaperReviewDetail> listAllByReviewAndModule(Long reviewId, long moduleId) {
		return repo.listAllByReviewAndModule(reviewId,moduleId);
	}

	public List<ClassRoomPaperReviewDetail> listAllByStudentAndModule(Long studentId, Long moduleId) {
		return repo.listAllByStudentAndModule(studentId,moduleId);
	}

	public int getCountByReview(Long cprId) {
		return repo.getCountByReview(cprId);
	}
}
