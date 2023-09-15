package com.rcs2.mcqsys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rcs2.mcqsys.model.Password;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.repository.UserRepository;
import com.rcs2.mcqsys.service.PasswordService;
import com.rcs2.mcqsys.service.RoleService;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordService passwordService;
	
	@Autowired
	private RoleService roledService;
	
	@Override
	public UserDetails loadUserByUsername(String username) 
	
			throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		
		Password password = new Password();
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}else {
			password = passwordService.getPasswordByUser(user.getUserId());
			return new MyUserDetails(user,password,roledService.listAll());
		}
		
		
	}

}

