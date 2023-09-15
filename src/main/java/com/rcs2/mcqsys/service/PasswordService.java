package com.rcs2.mcqsys.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Password;
import com.rcs2.mcqsys.repository.PasswordRepository;

@Service
@Transactional
public class PasswordService {
	
	@Autowired
	 private PasswordRepository repo;
	  
	 public List<Password> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Password password) {
	     repo.save(password);
	 }
	  
	 public Password get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public Password getPasswordByUser(Long userId) {
		return repo.getPasswordByUser(userId);
	}

	public int getDateDiff(LocalDate today, LocalDate lastUpdated) {
		return repo.getDateDiff(today,lastUpdated);
	}
}
