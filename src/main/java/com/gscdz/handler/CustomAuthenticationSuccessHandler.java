package com.gscdz.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.gscdz.model.User;
import com.gscdz.service.CustomUserService;
import com.gscdz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.gscdz.model.CustomUser;
import com.gscdz.repository.CustomUserRepository;
import com.gscdz.repository.UserRepository;
import org.springframework.web.bind.annotation.SessionAttributes;


@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	CustomUserService customUserService;
	@Autowired
	UserService userService;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			//读取对应的用户信息，存入session中
			CustomUser user=customUserService.findByUsername(authentication.getName());
			HttpSession session=request.getSession();
			session.setAttribute("userInf", user);
			response.sendRedirect("/getUserHome");
		}else {
			User admin = userService.findByUsername(authentication.getName());
			HttpSession session=request.getSession();
			session.setAttribute("adminInfo", admin);
			response.sendRedirect("/getAdminHome");
		}
		
	}

}
