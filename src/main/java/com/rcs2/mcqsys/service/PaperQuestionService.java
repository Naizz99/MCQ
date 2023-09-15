package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperQuestion;
import com.rcs2.mcqsys.repository.PaperQuestionRepository;

@Service
@Transactional
public class PaperQuestionService {
	
	@Autowired
	 private PaperQuestionRepository repo;
	  
	 public List<PaperQuestion> listAll() {
	     return repo.findAll();
	 }
	 
	 public List<PaperQuestion> listAllByPaperId(Long paperId) {
	     return repo.findAllById(paperId);
	 }
	  
	 public void save(PaperQuestion paperQuestion) {
	     repo.save(paperQuestion);
	 }
	  
	 public PaperQuestion get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<PaperQuestion> listAllByPaperId() {
		// TODO Auto-generated method stub
		return null;
	}

	public PaperQuestion getLastQuestionByPaper(Long paperID) {
		return repo.getLastQuestionByPaper(paperID);
	}

	public long[] listAllModulesByPaper(long paperID) {
		return repo.listAllModulesByPaper(paperID);
	}

	public List<PaperQuestion> listAllByPaperAndModule(Long paperId, long moduleId) {
		return repo.listAllByPaperAndModule(paperId,moduleId);
	}

	public int getCountByPaper(Long paperId) {
		return repo.getCountByPaper(paperId);
	}
}
