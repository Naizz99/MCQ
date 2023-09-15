package com.rcs2.mcqsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.PublisherUser;

public interface PublisherUserRepository extends JpaRepository<PublisherUser, Long>{

	@Query(value="SELECT * FROM publisher_user WHERE publisher_user.user_id = ?1 LIMIT 1",nativeQuery = true)
	PublisherUser getByUserId(Long userId);

	@Query(value="SELECT * FROM publisher_user WHERE publisher_user.po_id = ?1",nativeQuery = true)
	List<PublisherUser> listAllByPublisher(Long poId);

	@Query(value="SELECT * FROM publisher_user JOIN role ON role.role_id = publisher_user.role JOIN publisher ON publisher.po_id = publisher_user.po_id WHERE role.role = 'AUTHOR' AND publisher.po_id != 0",nativeQuery = true)
	List<PublisherUser> getAllAuthor();
	
	@Query(value="SELECT * FROM publisher_user JOIN role ON role.role_id = publisher_user.role JOIN publisher ON publisher.po_id = publisher_user.po_id WHERE role.role = 'EDITOR' AND publisher.po_id != 0;",nativeQuery = true)
	List<PublisherUser> getAllEditors();
	
	@Query(value="SELECT * FROM publisher_user JOIN role ON role.role_id = publisher_user.role WHERE po_id = ?1 AND role.role = 'AUTHOR'",nativeQuery = true)
	List<PublisherUser> listAuthorsByPublisher(Publisher poId);

	@Query(value="SELECT * FROM publisher_user JOIN role ON role.role_id = publisher_user.role WHERE po_id = ?1 AND role.role = 'EDITOR'",nativeQuery = true)
	List<PublisherUser> listEditorsByPublisher(Publisher poId);

	@Query(value="SELECT * FROM publisher_user WHERE user_id = ?1",nativeQuery = true)
	List<PublisherUser> listAllByUser(Long userId);

	@Query(value="SELECT * FROM publisher_user WHERE publisher_user.user_id = ?1 AND publisher_user.role = ?2",nativeQuery = true)
	PublisherUser getByUserAndType(Long userId, Long roleId);

	@Query(value="SELECT COUNT(publisher_user.pb_user_id) FROM publisher_user JOIN role ON role.role_id = publisher_user.role JOIN publisher ON publisher.po_id = publisher_user.po_id WHERE role.role = ?1 AND publisher.po_id = ?2",nativeQuery = true)
	int getCountByRoleAndPublisher(String role, Long poId);

}
