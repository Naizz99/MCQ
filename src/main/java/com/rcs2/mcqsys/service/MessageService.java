package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Message;
import com.rcs2.mcqsys.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
	 @Autowired
	 private MessageRepository repo;
	  
	 public List<Message> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Message message) {
	     repo.save(message);
	 }
	  
	 public Message get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	 public List<Message> listAll(Long userId, Long userId2) {
	     return repo.listAll(userId,userId2);
	 }
	 
	 public List<Message> allSenders(Long userId) {
		return repo.allSenders(userId);
	 }

	 public String getLastMessage(Long userId, Long userId2) {
		return repo.getLastMessage(userId,userId2);
	}

	public int unreadMessageCountByReceiver(Long userId) {
		return repo.unreadMessageCountByReceiver(userId);
	}

	public List<Message> listAllUnread(Long userId) {
		return repo.listAllUnread(userId);
	}
	
}
