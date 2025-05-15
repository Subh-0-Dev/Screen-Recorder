package com.subh.recorder.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.subh.recorder.entities.UserEntity;
import com.subh.recorder.repositories.UserRepository;

@Service
public class UserServices implements  UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> user=userRepository.findByUsername(username);
		if(user.isPresent()) {
			return User.builder()
					.username(user.get().getUsername())
					.password(user.get().getPassword())
					.build();
		}else {
			throw new UsernameNotFoundException(username);
		}
	}
	
}
