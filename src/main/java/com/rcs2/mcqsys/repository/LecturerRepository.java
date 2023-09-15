package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Lecturer;


public interface LecturerRepository  extends JpaRepository<Lecturer, Long>{
	
	@Query(value="SELECT * FROM lecturer WHERE lecturer.active = true",nativeQuery = true)
	List<Lecturer> findAllActive();

	@Query(value="SELECT COUNT(lecturer_id) FROM lecturer WHERE lecturer.lecturer_id = ?1",nativeQuery = true)
	int getLecturerCountById(Long lecturerId);

	@Query(value="SELECT * FROM lecturer WHERE lecturer.user_id = ?1",nativeQuery = true)
	Lecturer getLecturerByUserId(Long userId);

}
