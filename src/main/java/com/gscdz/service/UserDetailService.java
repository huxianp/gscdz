package com.gscdz.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gscdz.dto.UserInformation;
import com.gscdz.model.User;

@Service
@Slf4j
public class UserDetailService implements UserDetailsService{

	@Autowired
	UserService userService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=userService.findByUsername(username);
		if(user==null){
			throw new UsernameNotFoundException("USER NOT EXISTS");
		}
		return new UserInformation(user);
	}

}
