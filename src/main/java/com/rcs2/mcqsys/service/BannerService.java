package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.Banner;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.PublisherUser;
import com.rcs2.mcqsys.repository.BannerRepository;

@Service
@Transactional
public class BannerService {
	 @Autowired
	 private BannerRepository repo;
	  
	 public List<Banner> listAll() {
	     return repo.findAll();
	 }
	 
	 public void save(Banner banner) {
	     repo.save(banner);
	 }
	  
	 public Banner get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<Banner> listAllActive() {
		return repo.listAllActive();
	}

	public List<Banner> listAllNonGradeActive() {
		return repo.listAllNonGradeActive();
	}
	
	public List<Banner> listAllActiveByGrade(Long gradeId) {
		return repo.listAllActiveByGrade(gradeId);
	}

	public List<Banner> listAllByPublisher(Long poId) {
		return repo.listAllByPublisher(poId);
	}

	public List<Banner> listAllByLecturer(Long userId) {
		return repo.listAllByLecturer(userId);
	}

}
