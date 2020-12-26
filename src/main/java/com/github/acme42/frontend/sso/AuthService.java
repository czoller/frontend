package com.github.acme42.frontend.sso;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class AuthService {

	private final HttpSession session;

	@Autowired
	public AuthService(HttpSession session) {
		this.session = session;
	}
	
	public boolean isLoggedIn() {
		return session.getAttribute("user") != null;
	}
	
	public User getUser() {
		return (User) session.getAttribute("user");
	}
}
