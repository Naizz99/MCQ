package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.repository.StudentRepository;

@Service
@Transactional
public class StudentService {
	
	 @Autowired
	 private StudentRepository repo;

	 public List<Student> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Student student) {
	     repo.save(student);
	 }
	  
	 public Student get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<User> getAllStudent() {
		return repo.getAllStudent();
	}

	public Student getStudent(Long studentId) {
		return repo.getStudent(studentId);
	}

	public int countCompletedAttempts(Long studentId) {
		return repo.countCompletedAttempts(studentId);
	}

	public Student getStudentByUserId(Long userId) {
		return repo.getStudentByUserId(userId);
	}
	
	public List<Student> getStudentsByParent(Long parenId) {
		return repo.getStudentsByParent(parenId);
	}

	public List<Student> getStudentsByLecturer(Lecturer lecturerId) {
		return repo.getStudentsByLecturer(lecturerId);
	}
}
