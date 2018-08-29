package com.example.demo.audit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.audit.request.ChangePasswordRequest;
import com.example.demo.security.controller.respond.ApiResponse;
import com.example.demo.security.model.IUserRepository;
import com.example.demo.security.model.User;

@RestController
@RequestMapping("/audit")

public class AuditController {

	
	
	@Autowired
	IUserRepository userRepo;
	
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	
	
	@PostMapping("/password")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
		String uname = SecurityContextHolder.getContext().getAuthentication().getName();

		String name = request.getUsername();
		String newPasss = request.getPassword();
		System.out.println("======================== name = " + name  + "  pass " + newPasss);
		
		User user = userRepo.findByUsername(name).get();
		user.setPassword(passwordEncoder.encode(newPasss));
		
		User result = userRepo.save(user);

		return new ResponseEntity(new ApiResponse(true, "Password Change sccessfully!"), HttpStatus.OK );

		
	}
	
}
