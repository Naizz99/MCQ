package com.rcs2.mcqsys.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Password;

public interface PasswordRepository extends JpaRepository<Password, Long>{
	@Query(value="SELECT * FROM password WHERE password.user_id = ?1 AND password.active = true LIMIT 1",nativeQuery = true)
	Password getPasswordByUser(Long userId);

	@Query(value="SELECT DATEDIFF(?1, ?2)",nativeQuery = true)
	int getDateDiff(LocalDate today, LocalDate lastUpdated);
}
