package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.repository.LecturerRepository;

@Service
@Transactional
public class LecturerService {
	@Autowired
	 private LecturerRepository repo;
	  
	 public List<Lecturer> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Lecturer lecturer) {
	     repo.save(lecturer);
	 }
	  
	 public Lecturer get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }
	
	public List<Lecturer> listAllActive() {
		return repo.findAllActive();
	}

	public int getLecturerCountById(Long lecturerId) {
		return repo.getLecturerCountById(lecturerId);
	}

	public Lecturer getLecturerByUserId(Long userId) {
		return repo.getLecturerByUserId(userId);
	}
		
}
