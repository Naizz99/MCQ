package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.PaperPackage;

public interface PaperPackageRepository extends JpaRepository<PaperPackage, Long>{

	@Query(value="Select * from paper_package where paper_package.active = true AND paper_package.package_id <> 0",nativeQuery = true)
	List<PaperPackage> listAllForLecturer();

}
