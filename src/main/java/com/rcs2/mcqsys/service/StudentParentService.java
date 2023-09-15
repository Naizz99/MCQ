package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.StudentParent;
import com.rcs2.mcqsys.repository.StudentParentRepository;

@Service
@Transactional
public class StudentParentService {
	@Autowired
	private StudentParentRepository repo;
	  
	public List<StudentParent> listAll() {
		return repo.findAll();
	}
	  
	public void save(StudentParent parent) {
		repo.save(parent);
	}
	  
	public StudentParent get(long id) {
		return repo.findById(id).get();
	}
	  
	public void delete(long id) {
		repo.deleteById(id);
	}

	public int getAllStudentsForParent(Long long1) {
		return repo.getAllStudentsForParent(long1);
	}

	public List<StudentParent> listAllByParent(Long userId) {
		return repo.listAllByParent(userId);
	}

	public StudentParent getByStudentEmail(String studentEmail) {
		return repo.getByStudentEmail(studentEmail);
	}

	public List<StudentParent> getStudentsByParent(Long parenId) {
		return repo.getStudentsByParent(parenId);
	}

	public StudentParent getParentByStudent(Long userId) {
		return repo.getParentByStudent(userId);
	}

	public int getParentCountByStudent(Long userId) {
		return repo.getParentCountByStudent(userId);
	}

	public List<StudentParent> listAllByStudent(Long userId) {
		return repo.listAllByStudent(userId);
	}

	public int getCount(Long parentId, Long long1) {
		return repo.getCount(parentId,long1);
	}

	public StudentParent getByStudentAndParent(Long parentId, Long studentId) {
		return repo.getByStudentAndParent(parentId,studentId);
	}

	public StudentParent getByStudentUserId(Long userId) {
		return repo.getByStudentUserId(userId);
	}
}

