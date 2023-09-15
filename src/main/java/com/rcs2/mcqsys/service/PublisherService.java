package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.TempPublisher;
import com.rcs2.mcqsys.repository.PublisherRepository;
import com.rcs2.mcqsys.repository.TempPublisherRepository;

@Service
@Transactional
public class PublisherService {
	 @Autowired
	 private PublisherRepository repo;
	 	  
	 public List<Publisher> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(Publisher Publisher) {
	     repo.save(Publisher);
	 }
	 
	
	 public Publisher get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<Publisher> listAllActive() {
		return repo.findAllActive();
	}

	public int getPaperCount(Long poId) {
		return repo.getPaperCount(poId);
	}

	public int getPublisherCount() {
		return repo.getPublisherCount();
	}

	public List<Publisher> listActiveNonIndividual() {
		return repo.listActiveNonIndividual();
	}

	public List<Publisher> listAllNonIndividual() {
		return repo.listAllNonIndividual();
	}
	
}
