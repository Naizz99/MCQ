package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.RoleFunction;
import com.rcs2.mcqsys.repository.RoleFunctionRepository;

@Service
@Transactional
public class RoleFunctionService {
	 @Autowired
	 private RoleFunctionRepository repo;
	  
	 public List<RoleFunction> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(RoleFunction roleFunction) {
	     repo.save(roleFunction);
	 }
	  
	 public RoleFunction get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	 public List<RoleFunction> listAllByRole(Long roleId) {
		return repo.listAllByRole(roleId);
	 }

	 public RoleFunction getByRoleAndFunction(Long roleId, Long functionId) {
		return repo.getByRoleAndFunction(roleId,functionId);
	 }

	 public int getCountByRoleAndFunction(Long roleId, Long functionId) {
		return repo.getCountByRoleAndFunction(roleId,functionId);
	 }
	
	 public List<Long> getFunctionsForUser(long userId) {
		return repo.getFunctionsForUser(userId);
	 }

	 public RoleFunction getByFunction(Long functionId) {
		return repo.getByFunction(functionId);
	 }
}
