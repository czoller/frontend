package com.github.acme42.frontend.csrf;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class CsrfFilter implements Filter {
	
	private static final String TOKEN_SESSION_ATTR = "CSRF-Token";
	private static final String TOKEN_COOKIE_NAME = TOKEN_SESSION_ATTR;
	private static final String TOKEN_HEADER_NAME = "X-" + TOKEN_COOKIE_NAME;
	private static final int TOKEN_LENGTH_BYTES = 64;
	private final Logger logger;

	public CsrfFilter() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.info("SetCsrfTokenFilter");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		String token = getTokenFromSession(session);
		
		if (httpRequest.getMethod().equals("GET")) {
			if (token == null) {
				setNewToken(session, httpResponse);
			}
			chain.doFilter(request, response);
		}
		else if (token != null && checkHeader(httpRequest, token)) {
			chain.doFilter(request, response);
		}
		else {
			httpResponse.sendError(HttpStatus.SC_FORBIDDEN, "CSRF Failed: CSRF token missing or incorrect.");
		}
	}

	private String getTokenFromSession(HttpSession session) {
		return (String) session.getAttribute(TOKEN_SESSION_ATTR);
	}

	private void setTokenToSession(HttpSession session, String token) {
		session.setAttribute(TOKEN_SESSION_ATTR, token);
	}
	
	private void setNewToken(HttpSession session, HttpServletResponse response) {
		String token = computeToken(session);
		setTokenToSession(session, token);
		setCookie(response, token);
	}
	
	private void setCookie(HttpServletResponse httpResponse, String token) {
		Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
		cookie.setHttpOnly(false);
		cookie.setSecure(true);
		httpResponse.addCookie(cookie);
	}
	
	private boolean checkHeader(HttpServletRequest request, String token) {
		return token.equals(request.getHeader(TOKEN_HEADER_NAME));
	}
	
	private String computeToken(HttpSession session) {
		SecureRandom random = new SecureRandom();
		Base64.Encoder encoder = Base64.getUrlEncoder();
		byte[] values = new byte[TOKEN_LENGTH_BYTES];
		random.nextBytes(values);
		return encoder.encodeToString(values);
	}
}
