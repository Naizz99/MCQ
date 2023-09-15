package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Cart;
 
public interface CartRepository extends JpaRepository<Cart, Long> {

	@Query(value="SELECT * FROM cart WHERE cart.user_id = ?1",nativeQuery = true)
	List<Cart> listByUser(Long userId);

	@Query(value="SELECT COUNT(cart_id) FROM cart WHERE cart.user_id = ?1",nativeQuery = true)
	int getCountByUser(Long userId);
 
}