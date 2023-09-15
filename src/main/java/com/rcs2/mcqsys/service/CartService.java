package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Cart;
import com.rcs2.mcqsys.repository.CartRepository;

@Service
@Transactional
public class CartService {

	 @Autowired
	 private CartRepository repo;
	  
	 public List<Cart> listAll() {
	     return repo.findAll();
	 }
	 
	 public void save(Cart cart) {
	     repo.save(cart);
	 }
	  
	 public Cart get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	public List<Cart> listByUser(Long userId) {
		 return repo.listByUser(userId);
	}

	public int getCountByUser(Long userId) {
		return repo.getCountByUser(userId);
	}
}