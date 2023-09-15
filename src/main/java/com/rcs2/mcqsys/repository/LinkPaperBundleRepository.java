package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.LinkPaperBundle;

public interface LinkPaperBundleRepository extends JpaRepository<LinkPaperBundle, Long>{

	@Query(value="SELECT * FROM link_paper_bundle WHERE link_paper_bundle.bundle_id = ?1",nativeQuery = true)
	List<LinkPaperBundle> listAllByBundleId(Long bundleId);

	@Query(value="SELECT COUNT(pb_link_id) FROM link_paper_bundle WHERE link_paper_bundle.paper_id=?1 AND link_paper_bundle.bundle_id = ?2",nativeQuery = true)
	int getCount(Long paperId, Long bundleId);

}
