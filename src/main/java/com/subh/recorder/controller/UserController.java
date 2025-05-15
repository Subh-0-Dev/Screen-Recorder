package com.subh.recorder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.subh.recorder.entities.UserEntity;
import com.subh.recorder.repositories.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
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

	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    UserEntity savedUser = userRepository.save(user);
	    return ResponseEntity.ok(savedUser);
	}
	
}
