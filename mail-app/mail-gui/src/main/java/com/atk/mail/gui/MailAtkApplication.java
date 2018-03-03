package com.atk.mail.gui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.atk.mail.domain.repo.AccountRepo;
import com.atk.mail.domain.repo.UserRepo;
import com.atk.mail.entity.Account;
import com.atk.mail.entity.util.LocalDateJpaConverter;

@Configuration
@ComponentScan(basePackages = { "com.atk.mail" })
@EnableJpaRepositories(basePackageClasses = { UserRepo.class,AccountRepo.class})
@EntityScan(basePackageClasses = { Account.class, LocalDateJpaConverter.class })
@EnableAutoConfiguration
public class MailAtkApplication extends SpringBootServletInitializer {

	public static final String APP_URL = "/";
	public static final String LOGIN_URL = "/authentication/login.html";
	public static final String LOGOUT_URL = "/authentication/login.html?logout";
	public static final String LOGIN_FAILURE_URL = "/authentication/login.html?error";
	public static final String LOGIN_PROCESSING_URL = "/authentication/login";

	public static void main(String[] args) {
		SpringApplication.run(MailAtkApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MailAtkApplication.class);
	}
}
