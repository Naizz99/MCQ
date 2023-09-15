package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.ClassRoomStudent;
import com.rcs2.mcqsys.repository.ClassRoomStudentRepository;

@Service
@Transactional
public class ClassRoomStudentService {
	@Autowired
	 private ClassRoomStudentRepository repo;
	  
	 public List<ClassRoomStudent> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(ClassRoomStudent student) {
	     repo.save(student);
	 }
	  
	 public ClassRoomStudent get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<ClassRoomStudent> listAllByClassRoom(Long classroomId) {
		return repo.listAllByClassRoom(classroomId);
	}
	
	public List<ClassRoomStudent> listAllOnline(Long classroomId) {
		return repo.listAllOnline(classroomId);
	}

	public ClassRoomStudent getByStudentId(Long classroomId, Long studentId) {
		return repo.getByStudentId(classroomId,studentId);
	}
	
	public int getCountByLecture(Long userId) {
		return repo.getCountByLecture(userId);
	}

	public List<ClassRoomStudent> listAllOnlineByLecturer(Long userId) {
		return repo.listAllOnlineByLecturer(userId);
	}

	public List<ClassRoomStudent> listByUser(Long userId) {
		return repo.listByUser(userId);
	}

	public int getByStudentEmail(Long classroomId, String email) {
		return repo.listByUser(classroomId,email);
	}
}
