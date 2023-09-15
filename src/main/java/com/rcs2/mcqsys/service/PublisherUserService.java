package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.PublisherUser;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.repository.PublisherUserRepository;

@Service
@Transactional
public class PublisherUserService {
	 @Autowired
	 private PublisherUserRepository repo;
	  
	 public List<PublisherUser> listAll() {
	     return repo.findAll();
	 }
	 	  
	 public void save(PublisherUser pbUser) {
	     repo.save(pbUser);
	 }
	  
	 public PublisherUser get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public PublisherUser getByUserId(Long userId) {
		return repo.getByUserId(userId);
	}

	public List<PublisherUser> listAllByPublisher(Long poId) {
		return repo.listAllByPublisher(poId);
	}

	public List<PublisherUser> getAllAuthor() {
		return repo.getAllAuthor();
	}
	
	public List<PublisherUser> getAllEditors() {
		return repo.getAllEditors();
	}
	
	public List<PublisherUser> listAuthorsByPublisher(Publisher poId) {
		return repo.listAuthorsByPublisher(poId);
	}
	
	public List<PublisherUser> listEditorsByPublisher(Publisher poId) {
		return repo.listEditorsByPublisher(poId);
	}

	public List<PublisherUser> listAllByUser(Long userId) {
		return repo.listAllByUser(userId);
	}

	public PublisherUser getByUserAndType(Long userId, Long roleId) {
		return repo.getByUserAndType(userId,roleId);
	}

	public int getCountByRoleAndPublisher(String role, Long poId) {
		return repo.getCountByRoleAndPublisher(role,poId);
	}

}
