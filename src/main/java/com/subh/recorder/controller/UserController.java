package com.subh.recorder.controller;

import com.subh.recorder.DTOs.profileResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.subh.recorder.entities.UserEntity;
import com.subh.recorder.repositories.UserRepository;

import java.security.Principal;
import java.util.Optional;

@Controller
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserDetailsService userDetailsService;


	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/signup")
	public String signup() {
	    return "signup";
	}
	@GetMapping("/index")
	public String index() {
	    return "index";
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody UserEntity user) {
		if (userRepository.existsByUsername(user.getUsername())) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
	    }
		if (!user.getEmail().matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
	        return ResponseEntity.badRequest().body("Invalid email address");
	    }
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    UserEntity savedUser = userRepository.save(user);
	    return ResponseEntity.ok(savedUser);
	}
	@GetMapping("/api/user")
	public ResponseEntity<?> getUserProfile(Principal principal){
		Optional<UserEntity> userOpt = userRepository.findByUsername(principal.getName());
			UserEntity user= userOpt.get();
		return ResponseEntity.ok(new profileResponseDto(user.getId(),user.getUsername(),user.getEmail()));
	}


}





