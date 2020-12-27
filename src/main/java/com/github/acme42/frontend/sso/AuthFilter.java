package com.github.acme42.frontend.sso;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Super simplified login and authorization filter; usually provided by company shared library
 */
@Component
@Order(1)
public class AuthFilter implements Filter {
	
	private final Logger logger;

	public AuthFilter() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		if (httpRequest.getMethod().equals("POST") && httpRequest.getParameter("user") != null) {
			// Just for simplication, no decryption or verification here 
			User user = User.fromJSON(httpRequest.getParameter("user"));
			session.invalidate();
			session = httpRequest.getSession(true);
			session.setAttribute("user", user);
			logger.info("New login with user: " + user.getName());
			httpResponse.sendRedirect(httpRequest.getRequestURL().toString());
		}
		else if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			logger.debug("Already logged in with user: " + user.getName());
			chain.doFilter(request, response);
		}
		else {
			logger.warn("Access denied: " + httpRequest.getRequestURL());
			httpResponse.sendError(HttpStatus.SC_UNAUTHORIZED);
		}
	}
}
