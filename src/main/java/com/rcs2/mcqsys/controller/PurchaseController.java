package com.rcs2.mcqsys.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.rcs2.mcqsys.dto.PaperViewDto;
import com.rcs2.mcqsys.model.Cart;
import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.model.ClassRoomPackage;
import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.LinkPaperBundle;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperAnswer;
import com.rcs2.mcqsys.model.PaperBundle;
import com.rcs2.mcqsys.model.PaperPackage;
import com.rcs2.mcqsys.model.PaperQuestion;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.PublisherUser;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.UserPurchase;
import com.rcs2.mcqsys.repository.EmailService;
import com.rcs2.mcqsys.service.CartService;
import com.rcs2.mcqsys.service.ClassRoomPackageService;
import com.rcs2.mcqsys.service.ClassRoomService;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.UserPurchaseService;
import com.rcs2.mcqsys.service.LecturerService;
import com.rcs2.mcqsys.service.LinkPaperBundleService;
import com.rcs2.mcqsys.service.MessageService;
import com.rcs2.mcqsys.service.NotificationService;
import com.rcs2.mcqsys.service.PaperAnswerService;
import com.rcs2.mcqsys.service.PaperBundleService;
import com.rcs2.mcqsys.service.PaperPackageService;
import com.rcs2.mcqsys.service.PaperQuestionService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.PublisherUserService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.StudentParentService;
import com.rcs2.mcqsys.service.StudentService;
import com.rcs2.mcqsys.service.SubjectService;
import com.rcs2.mcqsys.service.UserService;
 
@Controller
public class PurchaseController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private StudentParentService studentParentService;
	
	@Autowired
	private PaperService paperService;
	
	@Autowired
	private PaperQuestionService paperQuestionService;
	
	@Autowired
	private PaperAnswerService paperAnswerService;
	
	@Autowired
	private PaperBundleService paperBundleService;
	
	@Autowired
	private LinkPaperBundleService linkPaperBundleService;
	
	@Autowired
	private PaperPackageService paperPackageService;
	
	@Autowired
	private ClassRoomPackageService classRoomPackageService;
	
	@Autowired
	private ClassRoomService classRoomService;
	
	@Autowired 
	private EmailService emailService;
	
	@Autowired 
	private NotificationService notificationService;
	
	@Autowired
	private LecturerService lecturerService;
	
	@Autowired
	private UserPurchaseService userPurchaseService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private PublisherUserService publisherUserService;
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private SimpMessagingTemplate webSoketTemplate;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MessageService messageService;
	
	EmailDetails email;
	Notification notification;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	
	@RequestMapping("/paperMarket")
	public String paperMarket(Model model,HttpSession session) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Paper> listPapers = paperService.listMarketAvailablePapers(user.getUsername());
		model.addAttribute("listPapers", listPapers);
		
		List<PaperBundle> listBundles = paperBundleService.listAvailableForMarket(user.getUserId());
		model.addAttribute("listBundles", listBundles);
	     
	    return "services/paper-market";
	}
	
	
	@RequestMapping("/listPurchasePapers")
	public String listPurchasePapers(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		List<UserPurchase> listPapers = userPurchaseService.listAllByUser(user.getUserId());
		model.addAttribute("listPapers", listPapers);
	    
		int purchaseCount = userPurchaseService.listAllCountByUser(user.getUserId());
	    if(purchaseCount > 0) {
	    	for(UserPurchase up : userPurchaseService.listAllByUser(user.getUserId())) {
	    		long noOfDays = Duration.between(LocalDate.now().atStartOfDay(), up.getEndDate().atStartOfDay()).toDays();
	    		if(noOfDays < 0) {
	    			up.setExpired(true);
	    			up.setAvailableDates(0);
	    		}else {
	    			up.setAvailableDates((int)noOfDays);
	    		}
	    		userPurchaseService.save(up);
	    	}
	    }
	    
	    List<Student> listStudents = new ArrayList<Student>();
	    
	    if((loggedRoles.contains(roleService.getByRole("PARENT")))) {
	    	listStudents = studentService.getStudentsByParent(user.getUserId());
	    }else if(loggedRoles.contains(roleService.getByRole("SUPERADMIN")) || loggedRoles.contains(roleService.getByRole("ADMIN"))) {
	    	listStudents = studentService.listAll();
	    }	    
		model.addAttribute("listStudents", listStudents);
		
	    return "services/purchased-paper-list";
	}
	
	@RequestMapping("/paperCart")
	public String paperCart(Model model,HttpSession session) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
	    return "services/paper-cart";
	}
	
	
	@GetMapping("/purchase")
	public ModelAndView purchase(@RequestParam Long paperId,@RequestParam Long bundleId,HttpSession session) {
		
		if(bundleId == 0) {
			ModelAndView mav = new ModelAndView("services/paper-purchase-preview");
			
			User user = (User)session.getAttribute("user");
			mav.addObject("user", user);
			
			List<PaperPackage> listPaperPackages = paperPackageService.listAllForLecturer();
			mav.addObject("listPaperPackages", listPaperPackages);
			
			Paper paper = paperService.get(paperId);
			mav.addObject("paper", paper);
			
			List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paperId);		
			mav.addObject("listAnswers", listPaperAnswers);
			
			List<PaperQuestion> listQuestions = paperQuestionService.listAllByPaperId(paperId);		
			List<PaperViewDto> listPaperQuestions = new ArrayList();
			
			for(PaperQuestion q:listQuestions) {
				PaperViewDto pv = new PaperViewDto();
				pv.setPqId(q.getPqId());
				pv.setQuestionId(q.getQuestionId());
				pv.setPaperID(q.getPaperID());
				pv.setModuleId(q.getModuleId());
				pv.setQuestion(q.getQuestion());
				pv.setImage(q.getImage());
				
				/*
				if(q.getImage() != null) {
					String path = (imageLocation + q.getImage()).replace('/', '\\');
					String base64String = null;
					
					byte[] byteData;
					try {
						byteData = Files.readAllBytes(Paths.get(path));
						base64String = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(byteData);
						pv.setBase64Img(base64String);
					} catch (IOException e) {
						e.printStackTrace();
						pv.setBase64Img(null);
					}
				}
				*/
				
				listPaperQuestions.add(pv);
			}
			mav.addObject("listQuestions", listPaperQuestions);
			
			return mav;
		}else{
			ModelAndView mav = new ModelAndView("services/bundle-purchase-preview");
			
			User user = (User)session.getAttribute("user");
			mav.addObject("user", user);
			
			List<PaperPackage> listPaperPackages = paperPackageService.listAllForLecturer();
			mav.addObject("listPaperPackages", listPaperPackages);
			
			PaperBundle bundle = paperBundleService.get(bundleId);
			mav.addObject("bundle", bundle);
			
			List<LinkPaperBundle> listPapers = linkPaperBundleService.listAllByBundleId(bundleId);
			mav.addObject("listPapers", listPapers);
			return mav;
		}
		
	}
	
	@RequestMapping("/paymentCheckout")
	public ModelAndView paymentCheckout(@RequestParam String type,@RequestParam long id,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/payment-chechout");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		ClassRoom classroom = new ClassRoom();
		Paper paper = new Paper();
		PaperBundle paperBundle = new PaperBundle();
		
		switch(type) {
		  case "classroom":
			classroom = classRoomService.get(id);
		    break;
		  case "paper":
		    paper = paperService.get(id);
		    break;
		  case "bundle":
			paperBundle = paperBundleService.get(id);
			break;
		  default:
		    // code block
		}
		
		mav.addObject("classroom", classroom);
		mav.addObject("paper", paper);
		mav.addObject("paperBundle", paperBundle);
		
		List<PaperPackage> listPaperPackages = paperPackageService.listAll();
		mav.addObject("listPaperPackages", listPaperPackages);
	     
		List<ClassRoomPackage> listClassRoomPackages = classRoomPackageService.listAll();
		mav.addObject("listClassRoomPackages", listClassRoomPackages);
	     
	    return mav;
	}
	
	@GetMapping("/checkout")
	public ResponseEntity<String> checkoutPaper(@RequestParam Long userId,@RequestParam Long id,@RequestParam char type,@RequestParam Long packageId,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		String invoice = null;
	    String subject = null;
	    
		boolean isDupplicate = false;
		UserPurchase oldPurchase = new UserPurchase();
		
		if((type == 'p') || (type == 'b')) {
			
			List<UserPurchase> listUserPaper = userPurchaseService.listAllByUser(userId);
			for (UserPurchase selectedPaper : listUserPaper) 
			{
				if((selectedPaper.getPaperId() != null) && ((selectedPaper.getPaperId().getPaperId()).equals(id))) {
					isDupplicate = true;
					oldPurchase = selectedPaper;
					break;
				}else if((selectedPaper.getBundleId() != null) && ((selectedPaper.getBundleId().getBundleId()).equals(id))) {
					isDupplicate = true;
					oldPurchase = selectedPaper;
					break;
				}
			}
			
			if(!isDupplicate) {
				UserPurchase userPurchase = new UserPurchase();
				
			    invoice = null;
			    subject = null;
			    
			    userPurchase.setUserId(user);
				
				if(type == 'p') {
					userPurchase.setType("paper");
					userPurchase.setPaperId(paperService.get(id));
					
					subject = "New Paper Purchased | " + paperService.get(id).getName();
					
					invoice =   "                             INVOICE                                     " 
								+ "\n\n-------------------------------------------------------------------"
								+ "\n| Product ID : " + paperService.get(id).getPaperId()
								+ "\n| Name        : " + paperService.get(id).getName()
								+ "\n| Quantity    : " + 1
								+ "\n| Rate          : " + paperPackageService.get(packageId).getPrice()
								+ "\n|------------------------------------------------------------------"
								+ "\n| Total Price  : " + paperPackageService.get(packageId).getPrice()
								+ "\n=================================";
							
				}
				if(type == 'b') {
					userPurchase.setType("bundle");
					userPurchase.setBundleId(paperBundleService.get(id));
					
					subject = "New Paper Bundle Purchased | " + paperBundleService.get(id).getName();
					
					invoice =   "                             INVOICE                                     " 
								+ "\n\n-------------------------------------------------------------------"
								+ "\n| Product ID : " + paperBundleService.get(id).getBundleId()
								+ "\n| Name        : " + paperBundleService.get(id).getName()
								+ "\n| Quantity    : " + 1
								+ "\n| Rate          : " + paperPackageService.get(packageId).getPrice()
								+ "\n|------------------------------------------------------------------"
								+ "\n| Total Price  : " + paperPackageService.get(packageId).getPrice()
								+ "\n=================================";
				}
					
				PaperPackage pPackage = paperPackageService.get(packageId);
				userPurchase.setPaperPackageId(pPackage);
				userPurchase.setStartDate(LocalDate.now());
				userPurchase.setActive(true);
				userPurchase.setEndDate(LocalDate.now().plusDays(pPackage.getDuration()));
				userPurchase.setAvailableDates((int)Duration.between(LocalDate.now().atStartOfDay(), userPurchase.getEndDate().atStartOfDay()).toDays());
	    		
				userPurchaseService.save(userPurchase);
								
				email = new EmailDetails(user.getEmail(),subject,invoice);
				emailService.sendMail(email);
				
				notification = new Notification(user,subject,("You have successfully purchase the paper/paper bundle for " + userPurchase.getPaperPackageId().getPrice()));
				notificationService.save(notification,session);
				
				return new ResponseEntity<>("success", HttpStatus.OK);
				
			}else{
				PaperPackage pPackage = paperPackageService.get(packageId);
				if((oldPurchase.getEndDate()).isBefore(LocalDate.now().plusDays(pPackage.getDuration()))) {
					oldPurchase.setPaperPackageId(pPackage);
					oldPurchase.setStartDate(LocalDate.now());
					oldPurchase.setActive(true);
				
					oldPurchase.setEndDate(LocalDate.now().plusDays(pPackage.getDuration()));
					oldPurchase.setAvailableDates((int)Duration.between(LocalDate.now().atStartOfDay(), oldPurchase.getEndDate().atStartOfDay()).toDays());
					oldPurchase.setExpired(false);
					userPurchaseService.save(oldPurchase);
					return new ResponseEntity<>("success", HttpStatus.OK);
				}else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}			
			}
		}else if(type == 'c') {
			ClassRoom classroom = classRoomService.get(id);
			classroom.setPackageId(classRoomPackageService.get(packageId));
			classroom.setActive(true);
			classroom.setExpired(false);
			
			classroom.setUpdateDate(LocalDate.now());
			classroom.setUpdateBy(user.getUsername());
			classroom.setEndDate(LocalDate.now().plusDays(classRoomPackageService.get(packageId).getDuration()));
			classroom.setAvailableDates((int)Duration.between(LocalDate.now().atStartOfDay(), (LocalDate.now().plusDays(classRoomPackageService.get(packageId).getDuration())).atStartOfDay()).toDays());
			classRoomService.save(classroom);
			
			subject = "Class Room Renewal | " + classroom.getName();
			
			invoice =   "                             INVOICE                                     " 
						+ "\n\n-------------------------------------------------------------------"
						+ "\n| Product ID : " + classroom.getClassroomId()
						+ "\n| Name        : " + classroom.getName()
						+ "\n| Action      : " + "Renewal"
						+ "\n| Rate          : " + classRoomPackageService.get(packageId).getPrice()
						+ "\n|------------------------------------------------------------------"
						+ "\n| Total Price  : " + classRoomPackageService.get(packageId).getPrice()
						+ "\n=================================";
			
			email = new EmailDetails(user.getEmail(),subject,invoice);
			emailService.sendMail(email);
			
			notification = new Notification(user,subject,("You have successfully renew the classroom " + classroom.getName() + " | Rs. " + classRoomPackageService.get(packageId).getPrice()));
			notificationService.save(notification,session);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestParam Long id,@RequestParam String type,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		
		boolean isDupplicate = false;
		
		List<Cart> listItems = cartService.listByUser(user.getUserId());
		for (Cart item : listItems) 
		{
			if((type.equals("paper")) && (item.getReferenceId().equals(paperService.get(id).getPaperId()))) {
				isDupplicate = true;
				break;
			}else if((type.equals("bundle")) && (item.getReferenceId().equals(paperBundleService.get(id).getBundleId()))) {
				isDupplicate = true;
				break;
			}
		}
		
		if(!isDupplicate) {
			Cart cart = new Cart();
		    cart.setType(type);
		    cart.setUserId(user);
		    cart.setReferenceId(id);
		    if(type.equals("paper")) {
		    	cart.setName(paperService.get(id).getName());
		    }else if(type.equals("bundle")) {
		    	cart.setName(paperBundleService.get(id).getName());
		    }else {
		    	cart.setName("-");
		    }
			cart.setActive(true);
			cart.setCreateBy(user.getUsername());
			cart.setCreateDate(LocalDate.now());
			cart.setUpdateBy(user.getUsername());
			cart.setUpdateDate(LocalDate.now());
			
			cartService.save(cart);
			
			int cartItemCount   = cartService.getCountByUser(user.getUserId());
			//Login.cartItemCount = cartItemCount;
		    session.setAttribute("cartItemCount", cartItemCount);
			
			webSoketTemplate.convertAndSend("/topic/updateCart",cartItemCount);	
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else{
			return new ResponseEntity<>("dupplicate", HttpStatus.OK);
		}
	}
	
	@RequestMapping("/listPaperPackages")
	public String listPaperPackages(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<PaperPackage> listPaperPackages = paperPackageService.listAll();
	    model.addAttribute("listPaperPackages", listPaperPackages);
	     
	    return "services/paper-package-list";
	}
	
	@GetMapping("/addPaperPackages")
	public ModelAndView addPaperPackages(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/paper-package-form");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		PaperPackage pPackage = new PaperPackage();
		pPackage.setCreateBy(user.getUsername());
		pPackage.setCreateDate(LocalDate.now());
		pPackage.setUpdateBy(user.getUsername());
		pPackage.setUpdateDate(LocalDate.now());
		
		mav.addObject("package", pPackage);	
		
		return mav;
	}
	
	@PostMapping("/secure/savePaperPackage")
	public ResponseEntity<Object> savePaperPackage(@ModelAttribute PaperPackage pPackage) {
		
		boolean isDuplicate = false;
		
		List<PaperPackage> packageList = paperPackageService.listAll();
		for(PaperPackage q: packageList) {
			if(q.getPackageId() == pPackage.getPackageId()) {
				isDuplicate = true;
				break;
			}
		}
		
		if(!isDuplicate) {
			pPackage.setCreateDate(LocalDate.now());			
		}
		pPackage.setUpdateDate(LocalDate.now());
		
		paperPackageService.save(pPackage);
					
		ResponseEntity<Object> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x ;
	}
	
	@GetMapping("/paperPackageDeactive")
	public String paperPackageDeactive(@RequestParam Long packageId) {
		PaperPackage pPackage = paperPackageService.get(packageId);
		pPackage.setActive(false);
		
		paperPackageService.save(pPackage);
		return "redirect:/listPaperPackages";
	}
	
	@GetMapping("/paperPackageActive")
	public String paperPackageActive(@RequestParam Long packageId) {
		PaperPackage pPackage = paperPackageService.get(packageId);
		pPackage.setActive(true);
		
		paperPackageService.save(pPackage);
		return "redirect:/listPaperPackages";
	}
	
	@GetMapping("/deletePaperPackage")
	public String deletePaperPackage(@RequestParam Long packageId) {		
		
		try {
			paperPackageService.delete(packageId);
			return "redirect:/listPaperPackages";
		} catch (Exception e) {
		    return "redirect:/listPaperPackages?dupplicate";
		}
		
	}
	
	@RequestMapping("/listClassRoomPackages")
	public String listClassRoomPackages(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<ClassRoomPackage> listPackages = classRoomPackageService.listAll();
	    model.addAttribute("listPackages", listPackages);
	     
	    return "services/classroom-package-list";
	}
	
	@GetMapping("/addClassRoomPackages")
	public ModelAndView addClassRoomPackages(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/classroom-package-form");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		ClassRoomPackage cPackage = new ClassRoomPackage();
		cPackage.setCreateBy(user.getUsername());
		cPackage.setCreateDate(LocalDate.now());
		cPackage.setUpdateBy(user.getUsername());
		cPackage.setUpdateDate(LocalDate.now());
		
		mav.addObject("package", cPackage);	
		
		return mav;
	}
	
	@PostMapping("/secure/saveClassRoomPackage")
	public ResponseEntity<Object> saveClassRoomPackage(@ModelAttribute ClassRoomPackage cPackage) {
		
		boolean isDuplicate = false;
		
		List<ClassRoomPackage> packageList = classRoomPackageService.listAll();
		for(ClassRoomPackage q: packageList) {
			if(q.getPackageId() == cPackage.getPackageId()) {
				isDuplicate = true;
				break;
			}
		}
		
		if(!isDuplicate) {
			cPackage.setCreateDate(LocalDate.now());			
		}
		cPackage.setUpdateDate(LocalDate.now());
		
		classRoomPackageService.save(cPackage);
					
		ResponseEntity<Object> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x ;
	}
	
	@GetMapping("/classRoomPackageDeactive")
	public String classRoomPackageDeactive(@RequestParam Long packageId) {
		ClassRoomPackage cPackage = classRoomPackageService.get(packageId);
		cPackage.setActive(false);
		
		classRoomPackageService.save(cPackage);
		return "redirect:/listClassRoomPackages";
	}
	
	@GetMapping("/classRoomPackageActive")
	public String classRoomPackageActive(@RequestParam Long packageId) {
		ClassRoomPackage cPackage = classRoomPackageService.get(packageId);
		cPackage.setActive(true);
		
		classRoomPackageService.save(cPackage);
		return "redirect:/listClassRoomPackages";
	}
	
	@GetMapping("/deleteClassRoomPackage")
	public String deleteClassRoomPackage(@RequestParam Long packageId) {		
		
		try {
			classRoomPackageService.delete(packageId);
			return "redirect:/listClassRoomPackages";
		} catch (Exception e) {
		    return "redirect:/listClassRoomPackages?dupplicate";
		}
		
	}
	
	@GetMapping("/deleteUserPurchase")
	public ResponseEntity<String> deleteUserPurchase(@RequestParam Long userPaperId) {
		userPurchaseService.delete(userPaperId);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@RequestMapping("/viewCart")
	public String viewCart(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Cart> itemList = cartService.listByUser(user.getUserId());
	    model.addAttribute("itemList", itemList);
	     
	    return "services/view-cart";
	}
	
	@GetMapping("/allocatePurchased")
	public ResponseEntity<String> allocatePurchased(@RequestParam Long id,@RequestParam String type,@RequestParam Long userId,@RequestParam Long packageId) {
		
		boolean isDupplicate = false;
		User user = userService.getById(userId);
		
		List<UserPurchase> listUserPaper = userPurchaseService.listAllByUser(userId);
		for (UserPurchase selectedPaper : listUserPaper) 
		{
			if((selectedPaper.getPaperId() != null) && ((selectedPaper.getPaperId().getPaperId()).equals(id))) {
				isDupplicate = true;
				break;
			}else if((selectedPaper.getBundleId() != null) && ((selectedPaper.getBundleId().getBundleId()).equals(id))) {
				isDupplicate = true;
				break;
			}
		}
		
		UserPurchase userPurchase = userPurchaseService.get(id);
		if(!isDupplicate) {
			UserPurchase userPaper = new UserPurchase();
			
			userPaper.setUserId(user);
			
			if(type.equals("paper")) {
				userPaper.setType("paper");
				userPaper.setPaperId(paperService.get(id));
			}
			if(type.equals("bundle")) {
				userPaper.setType("bundle");
				userPaper.setBundleId(paperBundleService.get(id));
			}
			
			userPaper.setPaperPackageId(paperPackageService.get(packageId));
			userPaper.setStartDate(LocalDate.now());
			userPaper.setActive(true);
			userPaper.setEndDate(userPurchase.getEndDate());
			
			userPurchaseService.save(userPaper);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else{
			return new ResponseEntity<>("dupplicate", HttpStatus.OK);
		}
	}
	
	@RequestMapping("/requestPapers")
	public String requestPapers(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Publisher> publisherList = publisherService.listActiveNonIndividual();
	    model.addAttribute("publisherList",publisherList);
	    
	    List<Lecturer> lecturerList = lecturerService.listAll();
	    model.addAttribute("lecturerList",lecturerList);
	    
	    List<Grade> gradeList = gradeService.listAllActive();
	    model.addAttribute("gradeList",gradeList);
	    
	    List<Subject> subjectList = subjectService.listAllActive();
	    model.addAttribute("subjectList",subjectList);
	     
	    return "services/request-paper";
	}
	
	@GetMapping("/sendPaperRequest")
	public ResponseEntity<String> sendPaperRequest(@RequestParam Long publisherId,@RequestParam Long lecturerId,@RequestParam String msg,HttpSession session) {	
		User user = (User)session.getAttribute("user");
		
		if(publisherId != 0) {
			Publisher publisher = publisherService.get(publisherId);
			List<PublisherUser> authorList = publisherUserService.listAllByPublisher(publisherId);
			for(PublisherUser pu : authorList) {
				email = new EmailDetails(
						pu.getUserId().getEmail(),
						("New Paper request | " + user.getName()),
						(" Name : " + user.getName() + "\n Request Date : " + LocalDate.now() + "\n Message : " + msg));
				emailService.sendMail(email);
				notification = new Notification(
						pu.getUserId(),
						("New Paper request | " + user.getName()),
						("Name : " + user.getName() + " | Request Date : " + LocalDate.now() + " | Message : " + msg));
				notificationService.save(notification,session);
			}
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else if(lecturerId != 0) {
			Lecturer lecturer = lecturerService.get(lecturerId);
			email = new EmailDetails(
					lecturer.getUserId().getEmail(),
					("New Paper request | " + user.getName()),
					(" Name : " + user.getName() + "\n Request Date : " + LocalDate.now() + "\n Message : " + msg));
			emailService.sendMail(email);
			notification = new Notification(
					lecturer.getUserId(),
					("New Paper request | " + user.getName()),
					("Name : " + user.getName() + " | Request Date : " + LocalDate.now() + " | Message : " + msg));
			notificationService.save(notification,session);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
}


