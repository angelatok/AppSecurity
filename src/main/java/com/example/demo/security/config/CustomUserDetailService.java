package com.example.demo.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.security.model.IUserRepository;
import com.example.demo.security.model.User;

@Service
public class CustomUserDetailService implements UserDetailsService {
	@Autowired 
	IUserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {

		User user = userRepo.findByUsernameOrEmail(userNameOrEmail, userNameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with : " + userNameOrEmail));
		//System.out.println("user found " + user.getName() + " " + user.getPassword());
		return user;
	}
	
	//used by JWTAuthenticationFilter
	public UserDetails loadUserById(String id){
		User user = userRepo.findById(id)
				.orElseThrow( () -> new UsernameNotFoundException("User not found with id : " + id));
System.out.println("user found by id " + user.getName() + user.getPassword());
		return user;
	}

}
