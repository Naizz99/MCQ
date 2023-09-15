package com.rcs2.mcqsys.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rcs2.mcqsys.config.CustomOAuth2User;
import com.rcs2.mcqsys.model.User;

public interface UserService {
	
	void processOAuthPostLogin(CustomOAuth2User oauthUser);
	
	List<User> findAll();
	
	void save(User user);
	
	User getById(long id);
		
	User getByUserName(String uname);
	
	void deleteById(long id);
	
	Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	
	List<User> findAllByRole(String role);
				
	int getCountByRole(String role);

	User getByEmail(String emailAddress);

	int getCountByEmail(String email);

	Long getParentByStudent(String studentEmail);
	
	int getParentCountByStudent(String studentEmail);

	int getParentCountByEmail(String prtEmail);

	User getParentByEmail(String prtEmail);

	int getCountByUserName(String string);

	int getCountByUserId(Long userId);

	int getCountByMobile(String input);

	List<User> getStudentsByMobile(String input);

	List<User> getStudentListByEmail(String email);

	User getStudentsByEmail(String studentEmail);

	int getStudentCountByEmail(String email);
}
