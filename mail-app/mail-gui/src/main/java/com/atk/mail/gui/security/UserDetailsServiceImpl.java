package com.atk.mail.gui.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.atk.mail.domain.repo.UserRepo;
import com.atk.mail.entity.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private  UserRepo userRepo;

	@Autowired
	public UserDetailsServiceImpl(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (null == user) {
			throw new UsernameNotFoundException("No user present with username: " + username);
		} else {
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("customerAdmin")));
		}
	}
	
	
}