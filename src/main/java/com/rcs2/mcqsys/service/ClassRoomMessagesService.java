package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.ClassRoomMessages;
import com.rcs2.mcqsys.repository.ClassRoomMessagesRepository;

@Service
@Transactional
public class ClassRoomMessagesService {
	@Autowired
	 private ClassRoomMessagesRepository repo;
	  
	 public List<ClassRoomMessages> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(ClassRoomMessages classRoomMessage) {
	     repo.save(classRoomMessage);
	 }
	  
	 public ClassRoomMessages get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<ClassRoomMessages> listAllByReceiver(String email) {
		return repo.listAllByReceiver(email);
	}

	public List<ClassRoomMessages> listAllUnreadByReceiver(String email) {
		return repo.listAllUnreadByReceiver(email);
	}

	public int unreadMessageCountByReceiver(String email) {
		return repo.unreadMessageCountByReceiver(email);
	}

	public List<ClassRoomMessages> allMessageByReceiver(String email, long classroomId) {
		return repo.allMessageByReceiver(email,classroomId);
	}

	public List<ClassRoomMessages> listAll(String sentFrom, String sentTo) {
		return repo.listAll(sentFrom,sentTo);
	}

	public List<ClassRoomMessages> allSenders(String email, long classroomId) {
		return repo.allSenders(email,classroomId);
	}

	public String getLastMessage(long classroomId,String email, String email2) {
		return repo.getLastMessage(classroomId,email,email2);
	}

	public List<ClassRoomMessages> listAllByClassRoom(Long classroomId) {
		return repo.listAllByClassRoom(classroomId);
	}
}
