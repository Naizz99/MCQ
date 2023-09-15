package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperBundle;
import com.rcs2.mcqsys.model.Publisher;

public interface PaperBundleRepository extends JpaRepository<PaperBundle, Long>{

	@Query(value="SELECT * FROM paper_bundle WHERE paper_bundle.available_for_buy = true AND paper_bundle.active=true AND paper_bundle.user_id != ?1",nativeQuery = true)
	List<PaperBundle> listAvailableForMarket(long userId);

	@Query(value="SELECT COUNT(bundle_id) FROM paper_bundle WHERE paper_bundle.bundle_id=?1",nativeQuery = true)
	int getCountBuId(Long bundleId);

	@Query(value="SELECT * FROM paper_bundle WHERE paper_bundle.publisher=?1",nativeQuery = true)
	List<PaperBundle> listAllByPublisher(Publisher publisher);

	@Query(value="SELECT * FROM paper_bundle WHERE paper_bundle.user_id=?1",nativeQuery = true)
	List<PaperBundle> listAllByUser(Long userId);

	@Query(value="SELECT * FROM paper_bundle WHERE paper_bundle.publisher=?1",nativeQuery = true)
	List<PaperBundle> listAllByPublisher(Long poId);

}
