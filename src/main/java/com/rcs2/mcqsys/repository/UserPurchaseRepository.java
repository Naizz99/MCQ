package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.UserPurchase;

public interface UserPurchaseRepository extends JpaRepository<UserPurchase, Long>{

	@Query(value="SELECT * FROM user_purchase WHERE user_purchase.user_id = ?1",nativeQuery = true)
	List<UserPurchase> listAllByUser(Long userId);

	@Query(value="SELECT * FROM user_purchase WHERE user_purchase.paper_id = ?1 AND user_purchase.user_id = ?2",nativeQuery = true)
	UserPurchase getByPaperAndUser(Long paperId,Long userId);
	
	@Query(value="SELECT * FROM user_purchase WHERE user_purchase.bundle_id = ?1 AND user_purchase.user_id = ?2",nativeQuery = true)
	UserPurchase getByBundleAndUser(Long bundleId,Long userId);

	@Query(value="SELECT COUNT(user_purchase.user_paper_id) FROM user_purchase WHERE user_purchase.user_id = ?1",nativeQuery = true)
	int listAllCountByUser(Long userId);

}
