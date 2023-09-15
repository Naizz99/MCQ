package com.rcs2.mcqsys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{

	@Query(value="SELECT * FROM profile WHERE profile.user = ?1",nativeQuery = true)
	Profile getByUserId(Long userId);

}
