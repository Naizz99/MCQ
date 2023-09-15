package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.TempPublisher;

public interface TempPublisherRepository extends JpaRepository<TempPublisher, Long> {
	
	@Query(value="SELECT * FROM temp_publisher WHERE temp_publisher.po_id = ?1",nativeQuery = true)
	TempPublisher getTempByPulisher(Long poId);

	@Query(value="SELECT COUNT(temp_id) FROM temp_publisher WHERE temp_publisher.po_id = ?1",nativeQuery = true)
	int getCountByPublisher(Long poId);
	
}
