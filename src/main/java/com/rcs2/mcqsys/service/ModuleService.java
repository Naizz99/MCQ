package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.repository.ModuleRepository;
import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Subject;

@Service
@Transactional
public class ModuleService {

	 @Autowired
	 private ModuleRepository repo;
	  
	 public List<Module> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Module module) {
	     repo.save(module);
	 }
	  
	 public Module get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }
	
	public List<Module> listAllActive() {
		return repo.findAllActive();
	}

	public List<Module> listAllActiveBySubject(Subject subject) {
		return repo.listAllActiveBySubject(subject);
	}

	public List<Module> listAllBySubject(Long subjectId) {
		return repo.listAllBySubject(subjectId);
	}
}