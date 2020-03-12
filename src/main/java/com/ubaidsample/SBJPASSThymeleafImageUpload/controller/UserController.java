package com.ubaidsample.SBJPASSThymeleafImageUpload.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ubaidsample.SBJPASSThymeleafImageUpload.model.User;
import com.ubaidsample.SBJPASSThymeleafImageUpload.repository.UserRepository;

@Controller
public class UserController {

	private final UserRepository userRepository;

	public UserController(UserRepository siteUserRepository) {
		this.userRepository = siteUserRepository;
	}

	@GetMapping("/register")
	public String register(Model model, SiteUserForm siteUserForm) {
		return "register";
	}

	@PostMapping("/register")
	public String register(Model model, User siteUser, SiteUserForm siteUserForm, BindingResult result) {
		
		if (siteUserForm.getName().matches("^[a-zA-Z0-9]{3,}$")){
			siteUser = new User(siteUserForm.getName().trim(), 
					new BCryptPasswordEncoder().encode(siteUserForm.getPassword()), true);
		}
		else {
			result.rejectValue("name", "username");
			return "register";
		}

		try {
			this.userRepository.save(siteUser);
		} catch(DataIntegrityViolationException ex){
			ex.printStackTrace();
			result.rejectValue("name", "name");
			return "register";
		}

		return "redirect:/login";
	}
	
	@GetMapping("/users")
	public String listUsers(Model model){
		model.addAttribute("users", userRepository.findAll());
		return "users";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
