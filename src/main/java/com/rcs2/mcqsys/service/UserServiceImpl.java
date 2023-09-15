package com.rcs2.mcqsys.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.config.CustomOAuth2User;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.Password;
import com.rcs2.mcqsys.model.Profile;
import com.rcs2.mcqsys.model.Provider;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.UserRole;
import com.rcs2.mcqsys.repository.PasswordRepository;
import com.rcs2.mcqsys.repository.RoleRepository;
import com.rcs2.mcqsys.repository.UserRepository;
import com.rcs2.mcqsys.repository.UserRoleRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordRepository passwordRepository;

	@Autowired
	private RoleRepository roleService;
	
	@Autowired
	private UserRoleRepository userRoleService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private NotificationService notificationService;
		
	@Override
	public List<User> findAll() {
		return this.userRepository.findAll();
	}

	@Override
	public void save(User user) {
		this.userRepository.save(user);
	}

	@Override
	public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.userRepository.findAll(pageable);
	}

	@Override
	public User getById(long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public void deleteById(long id) {
		
		for(Notification ntf : notificationService.listAll(id)) {
			notificationService.delete(ntf.getNtfId());
		}
				
		userRepository.deleteById(id);		
	}

	@Override
	public User getByUserName(String uname) {
		return userRepository.findByUsername(uname);
	}
	
	@Override
	public User getByEmail(String emailAddress) {
		return userRepository.getByEmail(emailAddress);
	}

	@Override
	public List<User> getStudentsByMobile(String mobile) {
		return userRepository.getStudentsByMobile(mobile);
	}
	
	@Override
	public List<User> findAllByRole(String role) {
		return userRepository.findAllByRole(role);
	}

	@Override
	public int getCountByRole(String role) {
		return userRepository.getCountByRole(role);
	}

	@Override
	public int getCountByEmail(String email) {
		return userRepository.getCountByEmail(email);
	}

	@Override
	public int getCountByMobile(String mobile) {
		return userRepository.getCountByMobile(mobile);
	}
	
	@Override
	public Long getParentByStudent(String studentEmail) {
		return userRepository.getParentByStudent(studentEmail);
	}

	@Override
	public int getParentCountByStudent(String studentEmail) {
		return userRepository.getParentCountByStudent(studentEmail);
	}

	@Override
	public int getParentCountByEmail(String prtEmail) {
		return userRepository.getParentCountByEmail(prtEmail);
	}

	@Override
	public int getStudentCountByEmail(String email) {
		return userRepository.getStudentCountByEmail(email);
	}
	
	@Override
	public User getParentByEmail(String prtEmail) {
		return userRepository.getParentByEmail(prtEmail);
	}

	@Override
	public int getCountByUserName(String username) {
		return userRepository.getCountByUserName(username);
	}

	@Override
	public int getCountByUserId(Long userId) {
		return userRepository.getCountByUserId(userId);
	}
	
	@Override
	public List<User> getStudentListByEmail(String email) {
		return userRepository.getStudentListByEmail(email.toLowerCase());
	}
	
	@Override
	public User getStudentsByEmail(String studentEmail) {
		return userRepository.getStudentsByEmail(studentEmail);
	}
	
	public void processOAuthPostLogin(CustomOAuth2User user) {
		
		User existUser = userRepository.getByEmail(user.getEmail());
				
		if (existUser == null) {
			User newUser = new User();
			newUser.setUsername(user.getEmail());
			newUser.setEmail(user.getEmail());
			newUser.setName(user.getName());
			newUser.setForcePassword(false);
			newUser.setProvider(Provider.GOOGLE);	
			newUser.setSso(true);
			newUser.setSsoEmail(user.getEmail());
			newUser.setActive(true);
			newUser.setCreateBy(user.getEmail());
			newUser.setCreateDate(LocalDate.now());
			newUser.setUpdateBy(user.getEmail());
			newUser.setUpdateDate(LocalDate.now());
			userRepository.save(newUser);
			
			Password password = new Password();
			password.setUserId(newUser);
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			password.setPassword(null);
			passwordRepository.save(password);
			
			UserRole uRole = new UserRole();
			uRole.setUserId(newUser);
			uRole.setRoleId(roleService.getByRole("GUEST"));
			userRoleService.save(uRole);
			
			Profile profile = new Profile();
			profile.setUser(newUser);
			profile.setProfilePic(null);
			profile.setCoverImage(null);
			profileService.save(profile);
		}
	}

	
}
