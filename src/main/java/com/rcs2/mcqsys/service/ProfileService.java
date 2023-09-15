package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.Profile;
import com.rcs2.mcqsys.repository.ProfileRepository;

@Service
@Transactional
public class ProfileService {
	 
	 @Autowired
	 private ProfileRepository repo;
	  
	 public List<Profile> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Profile profile) {
	     repo.save(profile);
	 }
	  
	 public Profile get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public Profile getByUserId(Long userId) {
		return repo.getByUserId(userId);
	}
	
}
