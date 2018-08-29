package com.example.demo.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.audit.request.ChangePasswordRequest;
import com.example.demo.security.config.JwtTokenProvider;
import com.example.demo.security.controller.respond.ApiResponse;
import com.example.demo.security.controller.respond.JwtAuthenticationResponse;
import com.example.demo.security.controller.resquest.LoginRequest;
import com.example.demo.security.controller.resquest.SignUpRequest;
import com.example.demo.security.model.IUserRepository;
import com.example.demo.security.model.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	IUserRepository userRepo;
	
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
		
		Authentication authentication = authenticationManager.authenticate(
										new UsernamePasswordAuthenticationToken(
												loginRequest.getUsernameOrEmail(), 
												loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.gernateToken(authentication);
		
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
	
		if(userRepo.existsByUsername(signUpRequest.getUsername())){
			return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
		}
		
		if(userRepo.existsByEmail(signUpRequest.getEmail())){
			return new ResponseEntity(new ApiResponse(false, "Email Address already in used!"), HttpStatus.BAD_REQUEST );
		}
		
		User user = new User(	signUpRequest.getName(), 
								signUpRequest.getUsername(), 
								signUpRequest.getEmail(),
								signUpRequest.getPassword()
								);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setAuthorities(AuthorityUtils.createAuthorityList("ROLE_USER"));
		User result = userRepo.save(user);

		return new ResponseEntity(new ApiResponse(true, "User registered sccessfully!"), HttpStatus.OK );

		
	}
}
