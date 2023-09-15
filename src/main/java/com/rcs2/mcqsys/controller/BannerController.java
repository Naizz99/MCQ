package com.rcs2.mcqsys.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.rcs2.mcqsys.dto.BannerDto;
import com.rcs2.mcqsys.model.Banner;
import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.model.ClassRoomBanner;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.BannerService;
import com.rcs2.mcqsys.service.ClassRoomBannerService;
import com.rcs2.mcqsys.service.ClassRoomService;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.LecturerService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.PublisherUserService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class BannerController {
	
	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LecturerService lecturerService;
	
	@Autowired
    private PublisherService publisherService;
	
	@Autowired
    private PublisherUserService publisherUserService;
		
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ClassRoomService classRoomService;
	
	@Autowired
	private ClassRoomBannerService classRoomBannerService;
	
	@Autowired
	private GradeService gradeService;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@RequestMapping("/listBanners")
	public String listBanners(Model model,HttpSession session) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Banner> listBanners = new ArrayList<Banner>();
		
		if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("AUTHOR")) || (roleService.listByUser(user.getUserId())).contains(roleService.getByRole("EDITOR"))) {
			listBanners = bannerService.listAllByPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId().getPoId());
		}
		if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("LECTURER"))) {
			listBanners = bannerService.listAllByLecturer(user.getUserId());
		}
		if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("ADMIN")) || (roleService.listByUser(user.getUserId())).contains(roleService.getByRole("SUPERADMIN"))) {
			listBanners = bannerService.listAll();
		}
	    
	    model.addAttribute("listBanners", listBanners);
	    
	    return "services/banner-list";
	}
	
	@GetMapping("/showBannerUpdate")
	public ModelAndView viewBannerUpdate(@RequestParam Long bannerId, HttpSession session) {
		ModelAndView mav = new ModelAndView("services/banner-form");
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		Banner banner = bannerService.get(bannerId);
		banner.setUpdateBy(user.getName());
		banner.setUpdateDate(LocalDate.now());
		
		if (loggedRoles.contains(roleService.getByRole("AUTHOR")) || (loggedRoles.contains(roleService.getByRole("EDITOR")))) {
			Publisher publisher = (publisherUserService.getByUserId(user.getUserId())).getPoId();
		    mav.addObject("publisher", publisher);
		}
		
		List<Publisher> publishers = publisherService.listActiveNonIndividual();
	    mav.addObject("publisherList", publishers);
	    
	    List<Role> roles = roleService.listAll();
	    mav.addObject("roles", roles);
		
	    if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			List<ClassRoom> listClassrooms = classRoomService.listAllByLecturer(lecturerService.getLecturerByUserId(user.getUserId()).getLecturerId());
			mav.addObject("listClassrooms", listClassrooms);
		}
	    
	    List<ClassRoomBanner> listBanners = classRoomBannerService.listAllByBanner(bannerId);
		mav.addObject("listBanners", listBanners);
	    
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);

		mav.addObject("banner", banner);
		return mav;
	}
		
	@GetMapping("/addBanner")
	public ModelAndView viewAddBanner(HttpSession session) {
		ModelAndView mav = new ModelAndView("services/banner-form");
		
		Banner banner = new Banner();
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		banner.setUserId(user);
		banner.setCreateDate(LocalDate.now());
		banner.setUpdateBy(user.getUsername());
		banner.setUpdateDate(LocalDate.now());
		mav.addObject("banner", banner);
		
		if (loggedRoles.contains(roleService.getByRole("AUTHOR")) || (loggedRoles.contains(roleService.getByRole("EDITOR")))) {
			Publisher publisher = (publisherUserService.getByUserId(user.getUserId())).getPoId();
		    mav.addObject("publisher", publisher);
		}
		
		List<Publisher> publishers = publisherService.listActiveNonIndividual();
	    mav.addObject("publisherList", publishers);
	    
	    List<Role> roles = roleService.listAll();
	    mav.addObject("roles", roles);
	    
	    //Get all grades
  		List<Grade> listGrades = gradeService.listAllActive();
  		mav.addObject("gradeList", listGrades);
	  		
		return mav;
	}
	
	@PostMapping("/secure/saveBanner")
	public ResponseEntity<String> saveBanner(@ModelAttribute BannerDto bannerDto) {	

		Banner banner;
		if(bannerDto.getBannerId() == 0) {
			banner = new Banner();
		}else {
			banner = bannerService.get(bannerDto.getBannerId());
		}
		
		banner.setHeader(bannerDto.getHeader());
		banner.setSubHeader(bannerDto.getSubHeader());
		banner.setContent(bannerDto.getContent());
		banner.setPublisherId(publisherService.get(bannerDto.getPublisherId()));
		banner.setGrade(gradeService.get(bannerDto.getGrade()));
		banner.setActive(false);
		banner.setCreateDate(bannerDto.getCreateDate());
		banner.setUpdateBy(bannerDto.getUpdateBy());
		banner.setUpdateDate(LocalDate.now());
		banner.setUserId(userService.getById(bannerDto.getUserId()));
		banner.setWidth(bannerDto.getWidth());
		banner.setHeight(bannerDto.getHeight());
		
		if(!((bannerDto.getImage()).equals(""))) {
			String fileName = bannerDto.getSerial() + "." + bannerDto.getExtension();
			byte[] decodedImg = Base64.getDecoder().decode(bannerDto.getImage().getBytes(StandardCharsets.UTF_8));
			Path destinationFile = Paths.get(imageLocation + "/banners", fileName);
			try {
				Files.write(destinationFile, decodedImg);
				banner.setImage("/banners/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		bannerService.save(banner);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/deleteBanner")
	public ResponseEntity<String> deleteBanner(@RequestParam Long bannerId) {
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		bannerService.delete(bannerId);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/bannerDeactive")
	public String bannerDeactive(@RequestParam Long bannerId) {
		Banner banner = bannerService.get(bannerId);
		banner.setActive(false);
		bannerService.save(banner);
		return "redirect:/listBanners";
	}
	
	@GetMapping("/bannerActive")
	public String bannerActive(@RequestParam Long bannerId) {
		Banner banner = bannerService.get(bannerId);
		banner.setActive(true);
		bannerService.save(banner);
		return "redirect:/listBanners";
	}
	
	@GetMapping("/addClassRoomBanner")
	public ResponseEntity<String> addClassRoomBanner(@RequestParam Long classroomId,@RequestParam Long bannerId) {
		ClassRoomBanner clsBanner = new ClassRoomBanner();
		clsBanner.setBannerId(bannerService.get(bannerId));
		clsBanner.setClassroomId(classRoomService.get(classroomId));
		
		for(ClassRoomBanner cb : classRoomBannerService.listAllByBanner(bannerId)) {
			if(cb.getClassroomId().getClassroomId() == classroomId) {
				return new ResponseEntity<>("dupplicate", HttpStatus.OK);
			}
		}
		
		classRoomBannerService.save(clsBanner);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/removeClassRoomBanner")
	public ResponseEntity<String> removeClassRoomBanner(@RequestParam Long classroomId,@RequestParam Long bannerId) {
		ClassRoomBanner clsBanner = classRoomBannerService.getByClassroomAndBanner(classroomId,bannerId);
	
		classRoomBannerService.delete(clsBanner.getCbId());
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
}
