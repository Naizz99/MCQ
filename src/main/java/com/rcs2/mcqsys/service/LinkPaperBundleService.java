package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.LinkPaperBundle;
import com.rcs2.mcqsys.repository.LinkPaperBundleRepository;

@Service
@Transactional
public class LinkPaperBundleService {
	 @Autowired
	 private LinkPaperBundleRepository repo;
	  
	 public List<LinkPaperBundle> listAll() {
	     return repo.findAll();
	 }
	  
	 public void save(LinkPaperBundle pbLink) {
	     repo.save(pbLink);
	 }
	  
	 public LinkPaperBundle get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<LinkPaperBundle> listAllByBundleId(Long bundleId) {
		return repo.listAllByBundleId(bundleId);
	}

	public int getCount(Long paperId, Long bundleId) {
		return repo.getCount(paperId,bundleId);
	}
}
