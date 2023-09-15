package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.ClassRoomBanner;
import com.rcs2.mcqsys.repository.ClassRoomBannerRepository;

@Service
@Transactional
public class ClassRoomBannerService {
	 @Autowired
	 private ClassRoomBannerRepository repo;
	  
	 public List<ClassRoomBanner> listAll() {
	     return repo.findAll();
	 }
	 
	 public void save(ClassRoomBanner banner) {
	     repo.save(banner);
	 }
	  
	 public ClassRoomBanner get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<ClassRoomBanner> listAllByUser(Long userId) {
		return repo.listAllByUser(userId);
	}
	
	public List<ClassRoomBanner> listAllByClassroom(Long classroomId) {
		return repo.listAllByClassroom(classroomId);
	}

	public List<ClassRoomBanner> listAllByBanner(Long bannerId) {
		return repo.listAllByBanner(bannerId);
	}

	public ClassRoomBanner getByClassroomAndBanner(Long classroomId, Long bannerId) {
		return repo.getByClassroomAndBanner(classroomId,bannerId);
	}
}
