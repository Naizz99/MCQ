package com.rcs2.mcqsys.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rcs2.mcqsys.dto.ProfileDto;
import com.rcs2.mcqsys.dto.PublisherRegisterDto;
import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.model.ClassRoomPackage;
import com.rcs2.mcqsys.model.ClassRoomPaper;
import com.rcs2.mcqsys.model.ClassRoomStudent;
import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperBundle;
import com.rcs2.mcqsys.model.Password;
import com.rcs2.mcqsys.model.Profile;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.PublisherUser;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.TempPublisher;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.UserPurchase;
import com.rcs2.mcqsys.model.UserRole;
import com.rcs2.mcqsys.repository.EmailService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.PublisherUserService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.TempPublisherService;
import com.rcs2.mcqsys.service.UserRoleService;
import com.rcs2.mcqsys.service.UserService;
import com.rcs2.mcqsys.service.NotificationService;
import com.rcs2.mcqsys.service.PaperBundleService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.PasswordService;
import com.rcs2.mcqsys.service.ProfileService;

@Controller
public class PublisherController {
	@Autowired
    private PublisherService publisherService;
    
    @Autowired
    private TempPublisherService tempPublisherService;
	
	@Autowired
	private PublisherUserService publisherUserService;
	
	@Autowired
	private PaperService paperService;
	
	@Autowired
	private PaperBundleService paperBundleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private PasswordService passwordService;
	
	@Autowired 
	private ProfileService profileService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;
		
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private EmailService emailService;
	
	@Autowired 
	private NotificationService notificationService;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	EmailDetails email;
	Notification notification;
	
	@RequestMapping("/listPublishers")
	public String listPublishers(Model model,HttpSession session) {
	    List<Publisher> listPublishers = publisherService.listAll();
	    model.addAttribute("listPublishers", listPublishers);
	     
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
	    return "publisher/publisher-list";
	}
		
	@GetMapping("/showPublisherUpdate")
	public ModelAndView viewPublisherUpdate(@RequestParam Long poId,HttpSession session) {			
		
		ModelAndView mav = new ModelAndView("publisher/publisher-form");
		Publisher publisher = publisherService.get(poId);
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		publisher.setUpdateBy(user.getName());
		publisher.setUpdateDate(LocalDate.now());
		
		mav.addObject("publisher", publisher);
		
		List<PublisherUser> listUsers = publisherUserService.listAllByPublisher(publisher.getPoId());
		mav.addObject("listUsers", listUsers);
		
		return mav;
	}
	
	@GetMapping("/addPublisher")
	public ModelAndView viewAddGrade(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("publisher/publisher-form");
		Publisher publisher = new Publisher();

		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		publisher.setActive(false);
		publisher.setApproved(true);
		publisher.setCreateBy(user.getUsername());
		publisher.setCreateDate(LocalDate.now());
		publisher.setUpdateBy(user.getUsername());
		publisher.setUpdateDate(LocalDate.now());
		
		mav.addObject("publisher", publisher);
		
		List<PublisherUser> listUsers = new ArrayList<PublisherUser>();
		mav.addObject("listUsers", listUsers);
		
		return mav;
	}
	
	@PostMapping("/secure/savePublisher")
	public ResponseEntity<String> savePublisher(@ModelAttribute Publisher publisher,HttpSession session) {
		User user = (User)session.getAttribute("user");
		
		publisher.setUpdateDate(LocalDate.now());
		publisher.setUpdateBy(user.getName());
		publisherService.save(publisher); 
				
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x ;
	}
	
	@PostMapping("/secure/registerPublisher")
	public ResponseEntity<String> registerPublisher(@ModelAttribute("publisher") PublisherRegisterDto publisher,HttpSession session){
		
		//Save Publisher
		Publisher pbl = new Publisher();
		pbl.setActive(true);
		pbl.setName(publisher.getCmpName());
		pbl.setAddress(publisher.getCmpAddress());
		pbl.setEmail(publisher.getCmpEmail());
		pbl.setMobile(publisher.getCmpMobile());
		pbl.setDescription(publisher.getCmpDescription());
		pbl.setActive(false);
		pbl.setApproved(false);
		pbl.setCreateBy(publisher.getCmpEmail());
		pbl.setCreateDate(LocalDate.now());
		pbl.setUpdateBy(publisher.getCmpEmail());
		pbl.setUpdateDate(LocalDate.now());
		publisherService.save(pbl); /**?**/
		
		
		TempPublisher tmpPbl = new TempPublisher();
		tmpPbl.setRejected(false);
		tmpPbl.setPoId(pbl);
		tempPublisherService.save(tmpPbl);
		
		User authorUser = new User();
		authorUser.setName(publisher.getCmpName());
		authorUser.setEmail(publisher.getCmpEmail());
		authorUser.setGender("male");
		authorUser.setMobile(publisher.getCmpMobile());
		authorUser.setUsername(publisher.getCmpEmail());
		authorUser.setActive(false);
		authorUser.setCreateBy(publisher.getCmpEmail());
		authorUser.setCreateDate(LocalDate.now());
		authorUser.setUpdateBy(publisher.getCmpEmail());
		authorUser.setUpdateDate(LocalDate.now());
		authorUser.setForcePassword(false);
		authorUser.setSso(false);
		authorUser.setSsoEmail(null);
		userService.save(authorUser);
		
		UserRole uRole = new UserRole();
		uRole.setUserId(authorUser);
		uRole.setRoleId(roleService.getByRole("AUTHOR"));
		userRoleService.save(uRole);
		
		Password password = new Password();
		password.setUserId(authorUser);
		password.setPassword(passwordEncoder.encode(publisher.getCmpPassword()));
		password.setActive(true);
		password.setLastUpdated(LocalDate.now());
		passwordService.save(password);
		
		Profile profile = new Profile();
		profile.setUser(authorUser);
		profile.setCoverImage(null);
		profile.setProfilePic(null);
		profileService.save(profile);
		
		PublisherUser author = new PublisherUser();
		author.setUserId(authorUser);
		author.setPoId(pbl);
		author.setRole(roleService.getByRole("AUTHOR"));
		publisherUserService.save(author);
		
		email = new EmailDetails(
				authorUser.getEmail(),
				("Publisher Registration - Request has been sent to the admin"),
				("Dear Sir/Madam, \n\n Request Sent Successfully!! \n \n Your request has been successfully sent to the admin. Please wait for the admin response. \n \n Company Name : " + publisher.getCmpName() + " \n Email : " +  publisher.getCmpEmail() + " \n Phone Number : " +  publisher.getCmpMobile() + " \n Address : " +  publisher.getCmpAddress()));
		emailService.sendMail(email);
		
		notification = new Notification(
				authorUser,
				("Publisher Registration - Request has been sent to the admin"),
				("Dear Sir/Madam, \n\n Request Sent Successfully!! \n \n Your request has been successfully sent to the admin. Please wait for the admin response. \n \n Company Name : " + publisher.getCmpName() + " \n Email : " +  publisher.getCmpEmail() + " \n Phone Number : " +  publisher.getCmpMobile() + " \n Address : " +  publisher.getCmpAddress()));
		notificationService.save(notification,session);
		
		List<User> adminList = userService.findAllByRole("ADMIN");
		for(User u : adminList) {
			email = new EmailDetails(
					u.getEmail(),
					("New publisher registration request | " + publisher.getCmpName()),
					(publisher.getCmpName() + " has been registered as a Publisher, \n\n Please approve the request of new publisher registration. and active the authors manually. \n \n Company Name : " + publisher.getCmpName() + " \n Email : " +  publisher.getCmpEmail() + " \n Phone Number : " +  publisher.getCmpMobile() + " \n Address : " +  publisher.getCmpAddress()));
			emailService.sendMail(email);
			
			notification = new Notification(
					u,
					("New publisher registration request | " + publisher.getCmpName()),
					(publisher.getCmpName() + " has been registered as a Publisher, \n\n Please approve the request of new publisher registration. and active the authors manually. \n \n Company Name : " + publisher.getCmpName() + " \n Email : " +  publisher.getCmpEmail() + " \n Phone Number : " +  publisher.getCmpMobile() + " \n Address : " +  publisher.getCmpAddress()));
			notificationService.save(notification,session);
			
		}
		
		return new ResponseEntity<>("success", HttpStatus.OK);
			
	}
		
	@GetMapping("/viewPublisher")
	public ModelAndView viewPublisher(@RequestParam Long poId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView();
		
		Publisher publisher = publisherService.get(poId);

		User user = new User();
		
		if(session.getAttribute("user") == null) {
			mav = new ModelAndView("publisher/publisher-view");
		}else {
			user = (User)session.getAttribute("user");
			
			if(((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("SUPERADMIN"))) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("ADMIN"))) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("LECTURER"))) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("AUTHOR")))) {
				mav = new ModelAndView("publisher/publisher-form");
				publisher.setUpdateBy(user.getName());
				publisher.setUpdateDate(LocalDate.now());
			}else if(((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("STUDENT"))) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("PARENT")))) {
				mav = new ModelAndView("publisher/publisher-view");
			}else {
				mav = new ModelAndView("publisher/publisher-form");
				publisher.setUpdateBy(user.getName());
				publisher.setUpdateDate(LocalDate.now());
			}
			
		}
		mav.addObject("user", user);	
		mav.addObject("publisher", publisher);
		
  		//Get all PublisherPapers
  		List<Paper> listPapers = paperService.listAllByPublisher(publisher);
  		mav.addObject("listPapers", listPapers);
  		mav.addObject("paperCount", listPapers.size());
  		
  		//Get all PublisherPapers
  		List<PaperBundle> listBundles = paperBundleService.listAllByPublisher(publisher);
  		mav.addObject("listBundles", listBundles);
  		
  		List<PublisherUser> listUsers = publisherUserService.listAllByPublisher(publisher.getPoId());
		mav.addObject("listUsers", listUsers);
  		
		return mav;
	}
	
	@GetMapping("/deletePublisher")
	public ResponseEntity<String> deletePublisher(@RequestParam Long poId) {

		boolean hasRelation = false;
		
		List<Paper> listPapers = paperService.listAll();
		for (Paper paper : listPapers) 
		{
			if((paper.getPublisher().getPoId()).equals(poId)) {
				hasRelation = true;
				break;
			}
		}
		
		List<PublisherUser> listUsers = publisherUserService.listAllByPublisher(poId);
		for (PublisherUser user : listUsers) 
		{
			publisherUserService.delete(user.getPbUserId());
		}
		
		if(hasRelation) {
			ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}else {
			ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
			
			if(tempPublisherService.getCountByPublisher(poId) > 0)
				tempPublisherService.delete(tempPublisherService.getTempByPulisher(poId).getTempId());
			publisherService.delete(poId);
			return x;
		}
	}
	
	@GetMapping("/publisherDeactive")
	public String publisherDeactive(@RequestParam Long poId) {
		Publisher publisher = publisherService.get(poId);
		publisher.setActive(false);
		publisherService.save(publisher); /**?**/
		return "redirect:/listPublishers";
	}
	
	@GetMapping("/publisherActive")
	public String publisherActive(@RequestParam Long poId) {
		Publisher publisher = publisherService.get(poId);
		publisher.setActive(true);
		publisherService.save(publisher); /**?**/
		return "redirect:/listPublishers";
	}
	
	@RequestMapping("/viewPublisherRequests")
	public String viewPublisherRequests(Model model,HttpSession session) {
		
	    List<TempPublisher> listPublishers = tempPublisherService.listAll();
	    model.addAttribute("listPublishers", listPublishers);
	     	    
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "publisher/publisher-approval-list";
	}
	
	@GetMapping("/deleteTempPublisher")
	public ResponseEntity<String> deleteTempPublisher(@RequestParam Long poId) {
		boolean hasRelation = false;
		
		tempPublisherService.delete(poId);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/publisherApprovalRequest")
	public ModelAndView publisherApprovalRequest(@RequestParam Long poId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("publisher/publisher-approval");
				
		TempPublisher tempPublisher = tempPublisherService.get(poId);
						
		Publisher publisher = publisherService.get(tempPublisher.getPoId().getPoId());
		mav.addObject("publisher", publisher);
				
		List<PublisherUser> listUsers = publisherUserService.listAllByPublisher(publisher.getPoId());
		mav.addObject("listUsers", listUsers);
				
		return mav;
	}
	
	@GetMapping("/approvePublisher")
	public ResponseEntity<String> approvePublisher(@RequestParam Long pbId,HttpSession session) {
		
		User loggeduser = (User)session.getAttribute("user");
				
		Publisher publisher = publisherService.get(pbId);
		publisher.setActive(true);
		publisher.setApproved(true);
		publisher.setUpdateBy(loggeduser.getUsername());
		publisher.setUpdateDate(LocalDate.now());
		publisherService.save(publisher); /**?**/
						
		TempPublisher tempPublisher = tempPublisherService.getTempByPulisher(pbId);
		tempPublisherService.delete(tempPublisher.getTempId());
				
		User author = new User();
		if(userService.getCountByEmail(publisher.getEmail()) > 0) {
			author = userService.getByEmail(publisher.getEmail());
			author.setActive(true);
			author.setUpdateBy(loggeduser.getUsername());
			author.setUpdateDate(LocalDate.now());
			userService.save(author);
		}
				
		email = new EmailDetails(
				author.getEmail(),
				("Registration Approved by admin | " + publisher.getName()),
				("Dear Sir/Madam, \n\n Your registration request has been approved by admin. You are now publisher in the System. Please login to the system and create Authors and Editors for your company."));
		emailService.sendMail(email);
		
		notification = new Notification(
				author,
				("Your registration request has been approved by Admin"),
				("You are now publisher in the System. Please login to the system and create Authors and Editors for your company."));
		notificationService.save(notification,session);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/rejectPublisher")
	public ResponseEntity<String> rejectPublisher(@RequestParam Long pbId,@RequestParam String note,HttpSession session) {
		
		User loggeduser = (User)session.getAttribute("user");

		TempPublisher tempPublisher = tempPublisherService.getTempByPulisher(pbId);
		tempPublisher.setRejected(true);
		tempPublisher.setNote(note);
		tempPublisherService.save(tempPublisher);
		
		Publisher publisher = publisherService.get(tempPublisher.getPoId().getPoId());
		publisher.setActive(false);
		publisher.setApproved(false);
		publisherService.save(publisher); /**?**/

		User author = new User();
		if(userService.getCountByEmail(publisher.getEmail()) > 0) {
			author = userService.getByEmail(publisher.getEmail());
			author.setActive(false);
			userService.save(author);
		}
		
		email = new EmailDetails(
				author.getEmail(),
				("Registration Rejected by admin | " + publisher.getName()),
				("Dear Sir/Madam, \n\n We regret to inform you that your request to register as a publisher has been rejected by the administrator. Please try again with modified submission form."));
		emailService.sendMail(email);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/activePublisherUser")
	public ResponseEntity<String> activePublisherUser(@RequestParam Long pbUserId,HttpSession session) {
		
		User loggedUser = (User)session.getAttribute("user");

		PublisherUser publisherUser = publisherUserService.get(pbUserId);

		User user = publisherUser.getUserId();
		user.setActive(true);
		user.setUpdateBy(loggedUser.getUsername());
		user.setUpdateDate(LocalDate.now());
		userService.save(user);
		
		email = new EmailDetails(
				user.getEmail(),
				("Your Account has been actived by Administrator | " + user.getName()),
				("Dear Sir/Madam, \n\n Your account has been activated by administrator. You are eliible for login to the system."));
		emailService.sendMail(email);
		
		notification = new Notification(
				user,
				("Your Account has been actived by Administrator"),
				("Your account has been activated by administrator."));
		notificationService.save(notification,session);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/deactivePublisherUser")
	public ResponseEntity<String> deactivePublisherUser(@RequestParam Long pbUserId,HttpSession session) {
		
		User loggedUser = (User)session.getAttribute("user");

		PublisherUser publisherUser = publisherUserService.get(pbUserId);

		User user = publisherUser.getUserId();
		user.setActive(false);
		user.setUpdateBy(loggedUser.getUsername());
		user.setUpdateDate(LocalDate.now());
		userService.save(user);
		
		email = new EmailDetails(
				user.getEmail(),
				("Account Deactivated | " + user.getName()),
				("Dear Sir/Madam, \n\n Your account has been deactivated by administrator. You are eliible for login to the system."));
		emailService.sendMail(email);
		
		notification = new Notification(
				user,
				("Account Deactivated!"),
				("Your account has been deactivated by administrator."));
		notificationService.save(notification,session);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/removePublisherUser")
	public ResponseEntity<String> removePublisherUser(@RequestParam Long pbUserId,HttpSession session) {
		
		User loggeduser = (User)session.getAttribute("user");

		PublisherUser publisherUser = publisherUserService.get(pbUserId);
		User user = publisherUser.getUserId();
		
		email = new EmailDetails(
				user.getEmail(),
				("Account Removed | " + user.getName()),
				("Dear Sir/Madam, \n\n Your account has been removed by administrator. You are not allowed for login to the system."));
		emailService.sendMail(email);
		
		publisherUserService.delete(publisherUser.getPbUserId());
		passwordService.delete(passwordService.getPasswordByUser(user.getUserId()).getUpId());
		for(UserRole ur : userRoleService.listByUser(user.getUserId())) {
			userRoleService.delete(ur.getUrId());
		}
		profileService.delete(profileService.getByUserId(user.getUserId()).getProfileId());
		userService.deleteById(user.getUserId());
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/activeAllPublisherUsers")
	public ResponseEntity<String> activeAllPublisherUsers(@RequestParam Long pbId,HttpSession session) {
		
		User loggeduser = (User)session.getAttribute("user");

		Publisher publisher = publisherService.get(pbId);

		List<PublisherUser> listUsers = publisherUserService.listAllByPublisher(publisher.getPoId());
		for(PublisherUser pbUser : listUsers) {
			User user = pbUser.getUserId();
			user.setActive(true);
			userService.save(user);
			
			email = new EmailDetails(
					user.getEmail(),
					("Your Account has been actived by Administrator | " + user.getName()),
					("Dear Sir/Madam, \n\n Your account has been activated by administrator. You are eliible for login to the system."));
			emailService.sendMail(email);
			
			notification = new Notification(
					user,
					("Your Account has been actived by Administrator"),
					("Your account has been activated by administrator."));
			notificationService.save(notification,session);
		}
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/deactiveAllPublisherUsers")
	public ResponseEntity<String> deactiveAllPublisherUsers(@RequestParam Long pbId,HttpSession session) {
		
		User loggeduser = (User)session.getAttribute("user");

		Publisher publisher = publisherService.get(pbId);

		List<PublisherUser> listUsers = publisherUserService.listAllByPublisher(publisher.getPoId());
		for(PublisherUser pbUser : listUsers) {
			User user = pbUser.getUserId();
			user.setActive(false);
			userService.save(user);
			
			email = new EmailDetails(
					user.getEmail(),
					("Account Deactivated | " + user.getName()),
					("Dear Sir/Madam, \n\n Your account has been deactivated by administrator. You are eliible for login to the system."));
			emailService.sendMail(email);
			
			notification = new Notification(
					user,
					("Account Deactivated!"),
					("Your account has been deactivated by administrator."));
			notificationService.save(notification,session);
		}
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/removeAllPublisherUsers")
	public ResponseEntity<String> removeAllPublisherUsers(@RequestParam Long pbId,HttpSession session) {
		
		User loggeduser = (User)session.getAttribute("user");

		Publisher publisher = publisherService.get(pbId);

		List<PublisherUser> listUsers = publisherUserService.listAllByPublisher(publisher.getPoId());
		
		for(PublisherUser pbUser : listUsers) {
			
			User user = pbUser.getUserId();

			email = new EmailDetails(
					user.getEmail(),
					("Account Removed | " + user.getName()),
					("Dear Sir/Madam, \n\n Your account has been removed by administrator. You are not allowed for login to the system."));
			emailService.sendMail(email);
			
			publisherUserService.delete(pbUser.getPbUserId());
			passwordService.delete(passwordService.getPasswordByUser(user.getUserId()).getUpId());
			for(UserRole ur : userRoleService.listByUser(user.getUserId())) {
				userRoleService.delete(ur.getUrId());
			}
			profileService.delete(profileService.getByUserId(user.getUserId()).getProfileId());
			userService.deleteById(user.getUserId());
		}
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@PostMapping("/secure/savePublisherPhoto")
	public ResponseEntity<String> savePublisherPhoto(@RequestParam String type , @RequestParam String data,@RequestParam String name,@RequestParam long publisherId,HttpSession session) {		
		User user = (User)session.getAttribute("user");
		Publisher publisher = publisherService.get(publisherId);
		
		if(!((data).equals(""))) {
			byte[] decodedImg = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
			if(type.equals("cover")) {
				Path destinationFile = Paths.get(imageLocation + "/publishers/cover-photos", name);
				try {
					Files.write(destinationFile, decodedImg);
					publisher.setCoverImage("/publishers/cover-photos/" + name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(type.equals("profile")) {
				Path destinationFile = Paths.get(imageLocation + "/publishers/profile-photos", name);
				try {
					Files.write(destinationFile, decodedImg);
					publisher.setImage("/publishers/profile-photos/" + name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		publisherService.save(publisher);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	@PostMapping("/secure/updatePublisherProfile")
	public ResponseEntity<Object> updateprofile(@ModelAttribute ProfileDto profileDto,HttpSession session) {
		
		Publisher publisher = publisherService.get(profileDto.getPublisherId());

		if(profileDto.getType().equals("new")) {
			publisher.setBodyBgColor(profileDto.getBodyBgColor());
			publisher.setBodyFontFamily(profileDto.getBodyFontFamily());
			publisher.setFontColor(profileDto.getFontColor());
			publisher.setCardBgColor(profileDto.getCardBgColor());
			publisher.setCardSideColor(profileDto.getCardSideColor1());
			publisher.setCardSelectedColor(profileDto.getCardSelectedColor());
		}else if(profileDto.getType().equals("default")) {
			publisher.setBodyBgColor("#f6f9ff");
			publisher.setBodyFontFamily("Nunito, sans-serif");
			publisher.setCardBgColor("#fff");
			publisher.setCardSideColor("#e1f0fa");
			publisher.setCardSelectedColor("#e1f0fc");
		}		

		publisherService.save(publisher);

		session.removeAttribute("publisher");
		session.setAttribute("publisher",publisher);
		
		return new ResponseEntity<>(publisher, HttpStatus.OK);
	}
	
}
