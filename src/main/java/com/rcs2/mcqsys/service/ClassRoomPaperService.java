package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.ClassRoomPaper;
import com.rcs2.mcqsys.repository.ClassRoomPaperRepository;

@Service
@Transactional
public class ClassRoomPaperService {
	@Autowired
	 private ClassRoomPaperRepository repo;
	  
	 public List<ClassRoomPaper> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(ClassRoomPaper paper) {
	     repo.save(paper);
	 }
	  
	 public ClassRoomPaper get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<ClassRoomPaper> listAllByClassRoom(Long classroomId) {
		return repo.listAllByClassRoom(classroomId);
	}

	public List<ClassRoomPaper> listAllActive(Long classroomId) {
		return repo.listAllActive(classroomId);
	}

}
