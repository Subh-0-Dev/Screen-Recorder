package com.subh.recorder.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.subh.recorder.Services.UserServices;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserServices userServices;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return userServices;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(userServices);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(httpForm->{
					httpForm.loginPage("/login").permitAll();
					httpForm.defaultSuccessUrl("/index");
				})
				.logout(logout -> logout
			            .logoutUrl("/logout")              
			            .logoutSuccessUrl("/login") 
			            .invalidateHttpSession(true)       
			            .deleteCookies("JSESSIONID")      
			        )
				.authorizeHttpRequests(registry->{
					registry.requestMatchers("/signup","/js/**").permitAll();
					registry.anyRequest().authenticated();
				})
				
				.build();
	}
}
