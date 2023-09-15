package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Functions;
import com.rcs2.mcqsys.repository.FunctionRepository;

@Service
@Transactional
public class FunctionService {
	 @Autowired
	 private FunctionRepository repo;
	  
	 public List<Functions> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Functions function) {
	     repo.save(function);
	 }
	  
	 public Functions get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<Functions> listParentFunctions() {
		return repo.listParentFunctions();
	}

	public List<Functions> listByParent(Long parentId) {
		return repo.listByParent(parentId);
	}
}
