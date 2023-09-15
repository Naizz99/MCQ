package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.PaperTemplate;
import com.rcs2.mcqsys.repository.PaperTemplateRepository;

@Service
@Transactional
public class PaperTemplateService {

	@Autowired
	 private PaperTemplateRepository repo;
	  
	 public List<PaperTemplate> listAll() {
	     return repo.findAll();
	 }
	 
}
