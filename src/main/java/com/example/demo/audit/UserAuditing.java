package com.example.demo.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserAuditing implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Authentication auth   = SecurityContextHolder.getContext().getAuthentication();
		if(!(auth instanceof AnonymousAuthenticationToken))
		{
			final UserDetails user = (UserDetails) auth.getPrincipal();
			name = user.getUsername();
		}

		System.out.println(" uname -= " + name);
		return Optional.of(name);
	}

}
