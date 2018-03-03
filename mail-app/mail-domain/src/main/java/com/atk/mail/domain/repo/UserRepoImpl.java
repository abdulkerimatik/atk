package com.atk.mail.domain.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.atk.mail.entity.User;


@Component
public class UserRepoImpl implements UserRepoCustom {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepo userRepo;
    
	public String encodePassword(String value) {
		return passwordEncoder.encode(value);
	}

    @Override
    public void saveUser(User user) {
    	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepo.save(user);
    }
	
}
