package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoomPackage;

public interface ClassRoomPackageRepository  extends JpaRepository<ClassRoomPackage, Long> {

	@Query(value="SELECT * FROM classroom_package WHERE classroom_package.active = true",nativeQuery = true)
	List<ClassRoomPackage> listAllActive();

}
