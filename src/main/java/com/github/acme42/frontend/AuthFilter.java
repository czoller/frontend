package com.github.acme42.frontend;

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

/**
 * Super simplified login and authorization filter; usually provided by company shared library
 */
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		if (httpRequest.getMethod().equals("POST") && httpRequest.getParameter("user") != null) {
			User user = User.fromJSON(httpRequest.getParameter("user"));
			session.invalidate();
			session = httpRequest.getSession(true);
			session.setAttribute("user", user);
			httpResponse.sendRedirect(httpRequest.getRequestURL().toString());
		}
		
		if (session.getAttribute("user") != null) {
			chain.doFilter(request, response);
		}
		else {
			httpResponse.sendError(HttpStatus.SC_UNAUTHORIZED);
		}
	}
}
