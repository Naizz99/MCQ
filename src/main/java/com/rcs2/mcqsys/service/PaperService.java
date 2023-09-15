package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.repository.PaperRepository;

@Service
@Transactional
public class PaperService {

	 @Autowired
	 private PaperRepository repo;
	  
	 public List<Paper> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Paper Paper) {
	     repo.save(Paper);
	 }
	  
	 public Paper get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }
	
	public List<Paper> listAllActive() {
		return repo.findAllActive();
	}
	
	public List<Paper> listAllByPublisher(Publisher poId) {
		return repo.listAllByPublisher(poId);
	}
	
	public List<Paper> studentListPublishersByGrade(Grade grade,Subject subject) {
		return repo.studentListPublishersByGrade(grade,subject);
	}
	
	public List<Paper> studentListPapersByPublisher(Grade grade,Long subjectId, Long poId) {
		return repo.studentListPapersByPublisher(grade,subjectId,poId);
	}

	public List<Paper> listAllByStatus() {
		return repo.listAllByStatus();
	}

	public int getTotalAttempts(long paperId) {
		return repo.getTotalAttempts(paperId);
	}
	
	public int getPaperCount() {
		return repo.getPaperCount();
	}

	public List<Paper> listAllForLecturer() {
		return repo.listAllForLecturer();
	}

	public List<Paper> listAllByUsername(String username) {
		return repo.listAllByUsername(username);
	}

	public List<Paper> listAllByLecturer(String username, Publisher publisher) {
		return repo.listAllByLecturer(username,publisher);
	}

	public List<Paper> listMarketAvailablePapers(String username) {
		return repo.listMarketAvailablePapers(username);
	}

	public Object getActivePapers() {
		return repo.getActivePapers();
	}
	
	public Object getDeactivePapers() {
		return repo.getDeactivePapers();
	}

	public List<Paper> listAllByPublisherGroupByCreateUser(Publisher poId) {
		return repo.listAllByPublisherGroupByCreateUser(poId);
	}

	public List<Paper> listAllGroupByCreateUser() {
		return repo.listAllGroupByCreateUser();
	}

	public int getPaperCountByUser(String createBy) {
		return repo.getPaperCountByUser(createBy);
	}

	public int getActivePaperCountByUser(String createBy) {
		return repo.getActivePaperCountByUser(createBy);
	}

	public int getDeactivePaperCountByUser(String createBy) {
		return repo.getDeactivePaperCountByUser(createBy);
	}

}