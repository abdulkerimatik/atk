package com.atk.mail.domain.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.atk.mail.dto.AccountDTO;
import com.atk.mail.entity.Account;
import com.atk.mail.entity.Role;
import com.atk.mail.entity.User;
import com.atk.mail.entity.util.Roles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccountRepoImpl implements AccountRepoCustom {
	
	@Autowired
	private PasswordEncoder  passwordEncoder;

	@Autowired
	private AccountRepo accountRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public void saveAccount(AccountDTO accountDTO){
		try {
			Account account=new Account();
			User user=new User();
			
			List<Role> roles=new ArrayList<>();
			Role role = roleRepo.findByCode(Roles.ACCOUNT_ADMIN);
			roles.add(role);
			user.setRoles(roles);
			
			account.setCommercialName(accountDTO.getCommercialName());
			account.setVknTckno(accountDTO.getVknTckno());
			account.setTaxOffice(accountDTO.getTaxOffice());
			account.setWebsite(accountDTO.getWebsite());
			account.setEmail(accountDTO.getEmail());
			
			account.setCreateDate(new Date());
			
			user.setUsername(accountDTO.getUsername());
			user.setAccount(account);
			String password = passwordEncoder.encode(accountDTO.getPassword());
			user.setPassword(password);
			user.setCreateDate(new Date());
			account.getUserList().add(user);
			
			accountRepo.save(account);
			log.info("AccountRepoImpl.saveAccount completed "+account.toString());
		} catch (Exception e) {
			throw e;
		}
	}
	
}
