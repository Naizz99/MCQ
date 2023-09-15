package com.rcs2.mcqsys.service;

import java.time.LocalTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.ClassRoomSessions;
import com.rcs2.mcqsys.repository.ClassRoomSessionsRepository;

@Service
@Transactional
public class ClassRoomSessionsService {
	 @Autowired
	 private ClassRoomSessionsRepository repo;
	  
	 public List<ClassRoomSessions> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(ClassRoomSessions classRoomSession) {
	     repo.save(classRoomSession);
	 }
	  
	 public ClassRoomSessions get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<ClassRoomSessions> listAllByClassRoom(long classroomId) {
		return repo.listAllByClassRoom(classroomId);
	}
	

	public LocalTime getTotalAttendTime(String studentEmail) {
		return repo.getTotalAttendTime(studentEmail);
	}

}
