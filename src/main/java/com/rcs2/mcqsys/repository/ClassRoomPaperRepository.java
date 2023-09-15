package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.ClassRoomPaper;

public interface ClassRoomPaperRepository  extends JpaRepository<ClassRoomPaper, Long>{

	@Query(value="SELECT * FROM classroom_paper WHERE classroom_paper.class_room_Id = ?1",nativeQuery = true)
	List<ClassRoomPaper> listAllByClassRoom(Long classroomId);

	@Query(value="SELECT * FROM classroom_paper WHERE classroom_paper.class_room_Id = ?1 AND classroom_paper.active = true",nativeQuery = true)
	List<ClassRoomPaper> listAllActive(Long classroomId);

}
