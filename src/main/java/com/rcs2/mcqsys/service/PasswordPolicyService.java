package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.PasswordPolicy;
import com.rcs2.mcqsys.repository.PasswordPolicyRepository;

@Service
@Transactional
public class PasswordPolicyService {
	 @Autowired
	 private PasswordPolicyRepository repo;
	  
	 public List<PasswordPolicy> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(PasswordPolicy policy) {
	     repo.save(policy);
	 }
	  
	 public PasswordPolicy get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }
}
