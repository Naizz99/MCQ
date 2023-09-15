package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.TempPublisher;
import com.rcs2.mcqsys.repository.TempPublisherRepository;

@Service
@Transactional
public class TempPublisherService {
	
	 @Autowired
	 private TempPublisherRepository repo;
	  
	 public List<TempPublisher> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(TempPublisher Publisher) {
	     repo.save(Publisher);
	 }
	 
	 public TempPublisher get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public TempPublisher getTempByPulisher(Long poId) {
		return repo.getTempByPulisher(poId);
	}

	public int getCountByPublisher(Long poId) {
		return repo.getCountByPublisher(poId);
	}
}
