package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.repository.ClassRoomRepository;

@Service
@Transactional
public class ClassRoomService {
	
	 @Autowired
	 private ClassRoomRepository repo;
	  
	 public List<ClassRoom> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(ClassRoom classRoom) {
	     repo.save(classRoom);
	 }
	  
	 public ClassRoom get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<ClassRoom> listAllByLecturer(Long userId) {
		return repo.listAllByLecturer(userId);
	}

	public int getAciveCount() {
		return repo.getAciveCount();
	}

	public int getAllCount() {
		return repo.getAllCount();
	}

	public int getCountByLecture(Long userId) {
		return repo.getCountByLecture(userId);
	}

	public int getActiveCountByLecture(Long userId) {
		return repo.getActiveCountByLecture(userId);
	}
	
	public List<ClassRoom> listAllByParent(Long userId) {
		return repo.listAllByParent(userId);
	}

	public ClassRoom getByLogginId(String logginId) {
		return repo.getByLogginId(logginId);
	}

}
