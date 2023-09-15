package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.ActiveSessions;
import com.rcs2.mcqsys.repository.ActiveSessionsRepository;

@Service
@Transactional
public class ActiveSessionsService {
	@Autowired
	 private ActiveSessionsRepository repo;
	  
	 public List<ActiveSessions> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(ActiveSessions activeSessions) {
	     repo.save(activeSessions);
	 }
	  
	 public ActiveSessions get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public ActiveSessions getActiveSessionByStudent(Long studentId) {
		return repo.getActiveSessionByStudent(studentId);
	}

	public int getActiveCount() {
		return repo.getActiveCount();
	}

	public List<ActiveSessions> listAllByPaperId(Long paperId) {
		return repo.listAllByPaperId(paperId);
	}
}
