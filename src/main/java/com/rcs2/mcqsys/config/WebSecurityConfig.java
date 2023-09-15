package com.rcs2.mcqsys.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.rcs2.mcqsys.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Autowired
	private UserService userService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/login","/model", "/oauth/**","/home","/validate","/index","/usersProfile","/registration","/registerPublisher","/login","/forgottenPassword","/sendOtp",
				"/changePassword","/classRoomLogin","/student/classroomLogin","/student/classroomLogout",
				"/mcq-websocket/info","/mcq-websocket/**","/secure/registerStudent","/secure/userValidation",
				"/getPaperWithCorrectAnswers","/showClassRoomMessages","/classroom/viewPaper","/getGrade",
				"/secure/emailValidation","/getPolicies","/viewClassRoom","/checkClassRoomStudentAvailability",
				"/registerClassRoomStudent","/viewPublisher","/secure/registerLecturer","/secure/sendMessage").permitAll()
			.antMatchers("/studentPage").hasAuthority("student")
			.antMatchers("/adminPage").hasAuthority("admin")
			.antMatchers("/superAdminPage").hasAuthority("superadmin")
			.anyRequest().authenticated()
			.and()
			.formLogin().permitAll()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/validate")
			.and()
			.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
					.userService(oauthUserService)
				.and()
				.successHandler(new AuthenticationSuccessHandler() {
					
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						
						CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
						
						userService.processOAuthPostLogin(oauthUser);
						
						response.sendRedirect("/validate");
					}
				})
				//.defaultSuccessUrl("/list")
			.and()
			.logout().logoutSuccessUrl("/index").permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/403")
			;
		
	}
	
	@Autowired
	private CustomOAuth2UserService oauthUserService;
	
	@Override 
	public void configure(WebSecurity web) throws Exception {
	      web.ignoring()
	              .antMatchers("/resources/**", "/static/**","/static/**","/static/**", "/plugin/**","/js/plugin/**","/css/**","/css/custom-css/**", "/js/**",
	            		  "/img/**","/img/questions/**","/img/banners/**","/img/bundle/**","/img/dashboard/**", "/webjars/**","/h2-console/**",
	                      "/saveUser/**","/vendor/**","/updateUser/**","/secure/**","/mcq-websocket/info","/mcq-websocket/**","publishers/**","/publishers/profile-photos/**","/publishers/cover-photos/**");
	}
}


