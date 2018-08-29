package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.security.model.IUserRepository;

@Component
public class MyCommandLineRunner implements CommandLineRunner{
	@Autowired
	private IUserRepository userRepo;
	
	
	
	@Override
	public void run(String... args) throws Exception {
		/*roleRepo.save(new Role(ERoleName.ROLE_USER));
		roleRepo.save(new Role(ERoleName.ROLE_ADMIN));*/
		
	}

}
