package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.TempPublisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	@Query(value="SELECT * FROM publisher WHERE publisher.active = true AND publisher.po_id != 0",nativeQuery = true)
	List<Publisher> findAllActive();

	@Query(value="SELECT COUNT(publisher) FROM paper WHERE paper.publisher = ?1",nativeQuery = true)
	int getPaperCount(Long poId);

	@Query(value="SELECT COUNT(po_id) FROM publisher",nativeQuery = true)
	int getPublisherCount();

	@Query(value="SELECT * FROM publisher WHERE publisher.active = true AND publisher.po_id != 0",nativeQuery = true)
	List<Publisher> listActiveNonIndividual();

	@Query(value="SELECT * FROM publisher WHERE publisher.po_id != 0",nativeQuery = true)
	List<Publisher> listAllNonIndividual();
}
