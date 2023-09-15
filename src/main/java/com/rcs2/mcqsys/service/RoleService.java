package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.repository.RoleRepository;

@Service
@Transactional
public class RoleService {
	@Autowired
	 private RoleRepository repo;
	  
	 public List<Role> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Role role) {
	     repo.save(role);
	 }
	  
	 public Role get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public Role getByRole(String string) {
		return repo.getByRole(string);
	}

	public List<Role> listByUser(Long userId) {
		return repo.listByUser(userId);
	}

	public List<String> listRoleNamesByUser(Long userId) {
		return repo.listRoleNamesByUser(userId);
	}

	public List<Role> listPublicRoles() {
		return repo.listPublicRoles();
	}

}
