package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.UserPurchase;
import com.rcs2.mcqsys.repository.UserPurchaseRepository;

@Service
public class UserPurchaseService {
	@Autowired
	 private UserPurchaseRepository repo;
	  
	 public List<UserPurchase> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(UserPurchase purchase) {
	     repo.save(purchase);
	 }
	  
	 public UserPurchase get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }
	
	 public List<UserPurchase> listAllByUser(Long userId) {
		return repo.listAllByUser(userId);
	 }
	 
	 public int listAllCountByUser(Long userId) {
		return repo.listAllCountByUser(userId);
	 }

	 public UserPurchase getByPaperAndUser(Long paperId, Long userId) {
		return repo.getByPaperAndUser(paperId,userId);
	 }
	 
	 public UserPurchase getByBundleAndUser(Long bundleId, Long userId) {
		return repo.getByBundleAndUser(bundleId,userId);
	 }
}
