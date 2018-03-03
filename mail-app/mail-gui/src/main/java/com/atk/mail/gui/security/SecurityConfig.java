package com.atk.mail.gui.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.atk.mail.entity.Role;
import com.atk.mail.gui.MailAtkApplication;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	private final RedirectAuthenticationSuccessHandler successHandler;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
			RedirectAuthenticationSuccessHandler successHandler) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.successHandler = successHandler;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Not using Spring CSRF here to be able to use plain HTML for the login
		// page
		http.csrf().disable();
		
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry reg = http
				.authorizeRequests();

		reg = reg.antMatchers("/forgot-password").permitAll();
		reg = reg.antMatchers("/recover-forgot-password").permitAll();
		reg = reg.antMatchers("/save-new-account").permitAll();
		reg = reg.antMatchers("/new-account").permitAll();
		reg = reg.antMatchers("/exception/access-denied").permitAll();
		reg = reg.antMatchers("/authentication/forgot-password").permitAll();
		reg = reg.antMatchers("/authentication/new-account").permitAll();
		
		reg = reg.antMatchers("/resources/**","/authentication/**").permitAll();
		// Require authentication for all URLS ("/**")
		reg = reg.antMatchers("/**").hasAnyAuthority(Role.getAllRoles());
		HttpSecurity sec = reg.and();
		reg.and().exceptionHandling().accessDeniedPage("/error");
		// Allow access to login page without login
		FormLoginConfigurer<HttpSecurity> login = sec.formLogin().permitAll();
		login = login.loginPage(MailAtkApplication.LOGIN_URL).loginProcessingUrl(MailAtkApplication.LOGIN_PROCESSING_URL)
				.failureUrl(MailAtkApplication.LOGIN_FAILURE_URL).successHandler(successHandler);
		
		login.and().logout().logoutSuccessUrl(MailAtkApplication.LOGOUT_URL);
	}

}
