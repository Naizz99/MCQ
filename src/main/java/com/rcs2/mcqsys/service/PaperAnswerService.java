package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.PaperAnswer;
import com.rcs2.mcqsys.repository.PaperAnswerRepository;

@Service
@Transactional
public class PaperAnswerService {
	@Autowired
	 private PaperAnswerRepository repo;
	  
	 public List<PaperAnswer> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(PaperAnswer paperAnswer) {
	     repo.save(paperAnswer);
	 }
	  
	 public PaperAnswer get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<PaperAnswer> listAllByQuestionId(Long pqId) {
		return repo.listAllByQuestionId(pqId);
	}

	public List<PaperAnswer> listAllByPaperId(Long paperId) {
		return repo.listAllByPaperId(paperId);
	}

	public PaperAnswer getCorrectAnswer(Long pqId) {
		return repo.getCorrectAnswer(pqId);
	}
}
