package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.UserRole;
import com.rcs2.mcqsys.repository.UserRoleRepository;

@Service
@Transactional
public class UserRoleService {
	 
	@Autowired
	 private UserRoleRepository repo;
	  
	 public List<UserRole> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(UserRole role) {
	     repo.save(role);
	 }
	  
	 public UserRole get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<UserRole> listByUser(Long adminId) {
		return repo.listByUser(adminId);
	}

	public int getCountByUserAndRole(Long userId, Long roleId) {
		return repo.getCountByUserAndRole(userId,roleId);
	}

	public int getCountByUserRole(Long uRoleId) {
		return repo.getCountByUserAndRole(uRoleId);
	}

	public UserRole getByUserAndRole(Long userId, Long roleId) {
		return repo.getByUserAndRole(userId,roleId);
	}

	public int getCountByUser(Long userId) {
		return repo.getCountByUser(userId);
	}

}
