package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoomBanner;

public interface ClassRoomBannerRepository extends JpaRepository<ClassRoomBanner, Long>{

	@Query(value="SELECT * FROM classroom_banner WHERE classroom_banner.user_id = ?1",nativeQuery = true)
	List<ClassRoomBanner> listAllByUser(Long userId);

	@Query(value="SELECT * FROM classroom_banner WHERE classroom_banner.classroom_id = ?1",nativeQuery = true)
	List<ClassRoomBanner> listAllByClassroom(Long classroomId);

	@Query(value="SELECT * FROM classroom_banner WHERE classroom_banner.banner_id = ?1",nativeQuery = true)
	List<ClassRoomBanner> listAllByBanner(Long bannerId);

	@Query(value="SELECT * FROM classroom_banner WHERE classroom_banner.classroom_id = ?1 AND classroom_banner.banner_id = ?2",nativeQuery = true)
	ClassRoomBanner getByClassroomAndBanner(Long classroomId, Long bannerId);
	

}
