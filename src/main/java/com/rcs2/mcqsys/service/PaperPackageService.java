package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.PaperPackage;
import com.rcs2.mcqsys.repository.PaperPackageRepository;

@Service
public class PaperPackageService {
	
	@Autowired
	 private PaperPackageRepository repo;
	  
	 public List<PaperPackage> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(PaperPackage PaperPackage) {
	     repo.save(PaperPackage);
	 }
	  
	 public PaperPackage get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<PaperPackage> listAllForLecturer() {
		 return repo.listAllForLecturer();
	}
	
}
