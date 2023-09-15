package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.PaperDetailAnswer;
import com.rcs2.mcqsys.repository.PaperDetailAnswerRepository;

@Service
@Transactional
public class PaperDetailAnswerService {
	@Autowired
	 private PaperDetailAnswerRepository repo;
	  
	 public List<PaperDetailAnswer> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(PaperDetailAnswer paperDetailAnswer) {
	     repo.save(paperDetailAnswer);
	 }
	  
	 public PaperDetailAnswer get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<PaperDetailAnswer> listByPaperId(Long paperId) {
		return repo.listByPaperId(paperId);
	}
}
