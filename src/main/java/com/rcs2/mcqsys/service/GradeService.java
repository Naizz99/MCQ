package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.repository.GradeRepository;

@Service
@Transactional
public class GradeService {

	 @Autowired
	 private GradeRepository repo;
	  
	 public List<Grade> listAll() {
	     return repo.findAll();
	 }
	 
	 public List<Grade> listAllActive() {
	     return repo.findAllActive();
	 }
	  
	 public void save(Grade Grade) {
	     repo.save(Grade);
	 }
	  
	 public Grade get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public Grade getByGrade(int grade) {
		return repo.getByGrade(grade);
	} 

}