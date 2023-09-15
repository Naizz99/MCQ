package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.ClassRoomPackage;
import com.rcs2.mcqsys.repository.ClassRoomPackageRepository;

@Service
public class ClassRoomPackageService {
	
	@Autowired
	private ClassRoomPackageRepository repo;
	
	public List<ClassRoomPackage> listAll() {
	     return repo.findAll();
	 }
	 
	 public void save(ClassRoomPackage packageId) {
	     repo.save(packageId);
	 }
	  
	 public ClassRoomPackage get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<ClassRoomPackage> listAllActive() {
		return repo.listAllActive();
	} 
}
