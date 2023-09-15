package com.rcs2.mcqsys.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.model.PaperBundle;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.repository.PaperBundleRepository;

@Service
@Transactional
public class PaperBundleService {
	@Autowired
	private PaperBundleRepository repo;
	  
	public List<PaperBundle> listAll() {
	    return repo.findAll();
	}
	  
	public void save(PaperBundle bundle) {
	    repo.save(bundle);
	}
	  
	public PaperBundle get(long id) {
	    return repo.findById(id).get();
	}
	  
	public void delete(long id) {
	    repo.deleteById(id);
	}

	public List<PaperBundle> listAvailableForMarket(long userId) {
		return repo.listAvailableForMarket(userId);
	}

	public int getCountBuId(Long bundleId) {
		return repo.getCountBuId(bundleId);
	}

	public List<PaperBundle> listAllByPublisher(Publisher publisher) {
		return repo.listAllByPublisher(publisher);
	}

	public List<PaperBundle> listAllByUser(Long userId) {
		return repo.listAllByUser(userId);
	}

	public List<PaperBundle> listAllByPublisher(Long poId) {
		return repo.listAllByPublisher(poId);
	}
}
