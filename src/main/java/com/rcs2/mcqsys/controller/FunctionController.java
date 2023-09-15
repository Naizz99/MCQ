package com.rcs2.mcqsys.controller;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.rcs2.mcqsys.model.Functions;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.RoleFunction;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.FunctionService;
import com.rcs2.mcqsys.service.RoleFunctionService;
import com.rcs2.mcqsys.service.RoleService;

@Controller
public class FunctionController {
	
	//User Function Management

	@Autowired
    private FunctionService functionService;
	
	@Autowired
    private RoleFunctionService roleFunctionService;
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping("/listFunctions")
	public String listFunctions(Model model,HttpSession session) {
		
		List<Functions> listFunctions = functionService.listAll();
	    model.addAttribute("listFunctions", listFunctions);
	    
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
				
	    return "user/function-list";
	}
	
	@GetMapping("/showFunctionUpdate")
	public ModelAndView showFunctionUpdate(@RequestParam Long functionId,HttpSession session) {		
		ModelAndView mav = new ModelAndView("user/function-form");
		Functions function = functionService.get(functionId);
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		List<Functions> listParentFunctions = functionService.listParentFunctions();
		mav.addObject("listParentFunctions", listParentFunctions);
		
		function.setUpdateBy(user.getUsername());
		function.setUpdateDate(LocalDate.now());
		mav.addObject("function", function);
		
		return mav;
	}
	
	@GetMapping("/addFunction")
	public ModelAndView addFunction(HttpSession session) {
		ModelAndView mav = new ModelAndView("user/function-form");
		Functions function = new Functions();
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		List<Functions> listParentFunctions = functionService.listParentFunctions();
		mav.addObject("listParentFunctions", listParentFunctions);
		
		function.setCreateBy(user.getUsername());
		function.setCreateDate(LocalDate.now());
		function.setUpdateBy(user.getUsername());
		function.setUpdateDate(LocalDate.now());
		function.setParent(false);
		mav.addObject("function", function);
		
		return mav;
	}
	
	@PostMapping("/secure/saveFunction")
	public ResponseEntity<String> saveUserFunction(@ModelAttribute Functions function) {
		function.setUpdateDate(LocalDate.now());
		
		if(function.getFunctionAction().equals("")) {
			function.setLinked(false);
		}else {
			function.setLinked(true);
		}
		
		functionService.save(function);
		
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x ;
	}
	
	@GetMapping("/deleteFunction")
	public ResponseEntity<String> deleteFunction(@RequestParam Long functionId) {
		functionService.delete(functionId);
		
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/functionDeactive")
	public String functionDeactive(@RequestParam Long functionId) {
		Functions function = functionService.get(functionId);
		function.setActive(false);
		functionService.save(function);
		return "redirect:/listFunctions";
	}
	
	@GetMapping("/functionActive")
	public String functionActive(@RequestParam Long functionId) {
		Functions function = functionService.get(functionId);
		function.setActive(true);
		functionService.save(function);
		return "redirect:/listFunctions";
	}
	
	//Role Management
	
	@RequestMapping("/listRoles")
	public String listRoles(Model model,HttpSession session) {
		List<Role> listRoles = roleService.listAll();
	    model.addAttribute("listRoles", listRoles);
	    		
		List<RoleFunction> listRoleFunctions = roleFunctionService.listAll();
	    model.addAttribute("listRoleFunctions", listRoleFunctions);
	    
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Role> roles = roleService.listAll();
		model.addAttribute("roles", roles);
		
	    return "user/role-management";
	}
	
	//Role - Function Management
	
	@RequestMapping("/listRoleFunctions")
	public String listRoleFunctions(Model model,HttpSession session) {
		List<Functions> listFunctions = functionService.listAll();
	    model.addAttribute("listFunctions", listFunctions);
	    		
		List<RoleFunction> listRoleFunctions = roleFunctionService.listAll();
	    model.addAttribute("listRoleFunctions", listRoleFunctions);
	    
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Role> roles = roleService.listAll();
		model.addAttribute("roles", roles);
		
	    return "user/role-functions";
	}
	
	@GetMapping("/listFunctionsByRole")
	public ResponseEntity<List<RoleFunction>> deleteRoleFunction(@RequestParam String roleName) {
		Role role = roleService.getByRole(roleName);
		List<RoleFunction> functionList = roleFunctionService.listAllByRole(role.getRoleId());
		ResponseEntity<List<RoleFunction>> x = new ResponseEntity<>(functionList, HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/showRoleFunctionUpdate")
	public ModelAndView showRoleFunctionUpdate(@RequestParam Long uflId,HttpSession session) {		
		ModelAndView mav = new ModelAndView("user/role-function-form");
		RoleFunction roleFunction = roleFunctionService.get(uflId);
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		roleFunction.setUpdateBy(user.getUsername());
		roleFunction.setUpdateDate(LocalDate.now());
		mav.addObject("roleFunction", roleFunction);
		
		return mav;
	}
	
	@GetMapping("/addRoleFunction")
	public ResponseEntity<String> addRoleFunction(@RequestParam long functionId, @RequestParam String role, HttpSession session) {
		
		boolean isDupplicate = false;
		User user = (User)session.getAttribute("user");
		List<RoleFunction> functionList = roleFunctionService.listAllByRole(roleService.getByRole(role).getRoleId());
		for(RoleFunction rf : functionList) {
			if(rf.getFunctionId().getFunctionId() == functionId) {
				isDupplicate = true;
			}
		}
		if(isDupplicate) {
			return new ResponseEntity<>("dupplicate", HttpStatus.OK);
		}else {
			RoleFunction roleFunction = new RoleFunction();
			roleFunction.setFunctionId(functionService.get(functionId));
			roleFunction.setRole(roleService.getByRole(role));
			roleFunction.setCreateBy(user.getUsername());
			roleFunction.setCreateDate(LocalDate.now());
			roleFunction.setUpdateBy(user.getUsername());
			roleFunction.setUpdateDate(LocalDate.now());
			roleFunction.setActive(true);
			roleFunctionService.save(roleFunction);
			if(functionService.get(functionId).isParent()) {
				for(Functions f : functionService.listByParent(functionId)) {
					roleFunction = new RoleFunction();
					
					roleFunction.setFunctionId(f);
					roleFunction.setRole(roleService.getByRole(role));
					roleFunction.setCreateBy(user.getUsername());
					roleFunction.setCreateDate(LocalDate.now());
					roleFunction.setUpdateBy(user.getUsername());
					roleFunction.setUpdateDate(LocalDate.now());
					roleFunction.setActive(true);
					
					roleFunctionService.save(roleFunction);
				}
			}
			return new ResponseEntity<>("success", HttpStatus.OK);
		}
	}
	
	@GetMapping("/deleteRoleFunction")
	public ResponseEntity<String> deleteRoleFunction(@RequestParam Long uflId) {
		
		if(roleFunctionService.get(uflId).getFunctionId().isParent()) {
			for(Functions f : functionService.listByParent(roleFunctionService.get(uflId).getFunctionId().getFunctionId())) {
				RoleFunction rf = roleFunctionService.getByRoleAndFunction(roleFunctionService.get(uflId).getRole().getRoleId(),f.getFunctionId());
				
				if(rf != null) {
					roleFunctionService.delete(rf.getUflId());
				}
			}
		}
		
		roleFunctionService.delete(uflId);
				
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/roleFunctionDeactive")
	public String roleFunctionDeactive(@RequestParam Long uflId) {
		RoleFunction roleFunction = roleFunctionService.get(uflId);
		roleFunction.setActive(false);
		roleFunctionService.save(roleFunction);
		return "redirect:/listRoleFunctions";
	}
	
	@GetMapping("/roleFunctionActive")
	public String roleFunctionActive(@RequestParam Long uflId) {
		RoleFunction roleFunction = roleFunctionService.get(uflId);
		roleFunction.setActive(true);
		roleFunctionService.save(roleFunction);
		return "redirect:/listRoleFunctions";
	}

}
