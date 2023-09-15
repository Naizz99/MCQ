package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.repository.SubjectRepository;

@Service
@Transactional
public class SubjectService {

	 @Autowired
	 private SubjectRepository repo;
	  
	 public List<Subject> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Subject subject) {
	     repo.save(subject);
	 }
	  
	 public Subject get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }
	 
	 public List<Subject> listAllSubjectByGrade(long gradeId) {
	     return repo.listAllSubjectByGrade(gradeId);
		 //return repo.findAll();
	 }
	
	public List<Subject> listAllActive() {
		return repo.findAllActive();
	}
	
	public List<Subject> studentListSubjectsByGrade(Grade grade) {
		return repo.studentListSubjectsByGrade(grade);
	}

	public List<Subject> listByStudentPaperReview(Long studentId) {
		return repo.listByStudentPaperReview(studentId);
	}

	public List<Subject> listByClassRoomPaperReview(Long studentId) {
		return repo.listByClassRoomPaperReview(studentId);
	}

	public List<Subject> listByClassroomPaperReview(Long classroomId, Long studentId) {
		return repo.listByClassRoomPaperReview(classroomId,studentId);
	}

}