package com.rcs2.mcqsys.config;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.PasswordService;

public class CustomUserDetails implements UserDetails{
	private static final long serialVersionUID = 1L;
	private User user;
	
	@Autowired
	private PasswordService passwordService;
	
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override 
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//return Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRole()));
		return null;
	}

	@Override
	public String getPassword() {
		return passwordService.getPasswordByUser(user.getUserId()).getPassword();
	}
	
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Long getUserId() {
		return user.getUserId();
	}

	public String getName() {
		return user.getName();
	}

	public String getGender() {
		return user.getGender();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public String getMobile() {
		return user.getMobile();
	}

	public boolean isActive() {
		return user.isActive();
	}

	public String getCreateBy() {
		return user.getCreateBy();
	}
	
	public LocalDate getCreateDate() {
		return user.getCreateDate();
	}

	public String getUpdateBy() {
		return user.getUpdateBy();
	}
	
	public LocalDate getUpdateDate() {
		return user.getCreateDate();
	}

	
}
