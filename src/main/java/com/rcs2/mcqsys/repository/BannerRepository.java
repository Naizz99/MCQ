package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Banner;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.PublisherUser;

public interface BannerRepository extends JpaRepository<Banner, Long>{

	@Query(value="SELECT * FROM banner WHERE banner.active = true",nativeQuery = true)
	List<Banner> listAllActive();

	@Query(value="SELECT * FROM banner WHERE banner.grade_id IS NULL AND banner.active = true",nativeQuery = true)
	List<Banner> listAllNonGradeActive();
	
	@Query(value="SELECT * FROM banner WHERE banner.grade_id = ?1 AND banner.active = true",nativeQuery = true)
	List<Banner> listAllActiveByGrade(Long gradeId);

	@Query(value="SELECT * FROM banner WHERE banner.publisher_id = ?1",nativeQuery = true)
	List<Banner> listAllByPublisher(long poId);

	@Query(value="SELECT * FROM banner WHERE banner.user_id = ?1",nativeQuery = true)
	List<Banner> listAllByLecturer(Long userId);

}
