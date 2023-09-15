package com.rcs2.mcqsys.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.UserService;

@Component
public class MCQAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		boolean isStudent = false;
		boolean isParent = false;
		boolean isUser = false;
		boolean isAuthor = false;
		boolean isAdmin = false;
		boolean isSuperAdmin = false;
		boolean isLecturer = false;
		
		boolean isActive = false;
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		User user = userService.getByUserName(MCQAuthenticationSuccessHandler.getName());

		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("STUDENT")) {
				isStudent = true;
				break;
			}else if (grantedAuthority.getAuthority().equals("PARENT")) {
				isParent = true;
				break;
			}else if (grantedAuthority.getAuthority().equals("AUTHOR")) {
				isAuthor = true;
				break;
			}else if (grantedAuthority.getAuthority().equals("EDITOR")) {
				isUser = true;
				break;
			}else if (grantedAuthority.getAuthority().equals("ADMIN")) {
				isAdmin = true; 
				break;
			}else if (grantedAuthority.getAuthority().equals("SUPERADMIN")) {
				isSuperAdmin = true;
				break;
			}else if (grantedAuthority.getAuthority().equals("LECTURER")) {
				isLecturer = true;
				break;
			}
		}
		
		
		isActive = user.isActive();
		
		
		if ((isStudent || isUser || isAuthor || isAdmin || isSuperAdmin || isLecturer || isParent) && (isActive)) {
			user.setLogged(true);
			userService.save(user);
			redirectStrategy.sendRedirect(request, response, "/validate");
		} else if(!isActive){
			redirectStrategy.sendRedirect(request, response, "/logout");
		}else {
			redirectStrategy.sendRedirect(request, response, "/pageNotFound");
		}
	}
	
	public static boolean isLogged() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication && !("anonymousUser").equals(authentication.getName());
    }

	public static String hasRole() {
		// TODO Auto-generated method stub
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			return authentication.getAuthorities().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return null;
	}
	
	public static String getName() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentLoggedName = authentication.getName();
				
		return currentLoggedName;
	}
	
	public static String getLoggedUserName() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentLoggedName = authentication.getName();
				
		return currentLoggedName;
	}

	
}
