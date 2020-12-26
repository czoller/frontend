package com.github.acme42.frontend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.github.acme42.frontend.sso.AuthService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
@SessionScope
public class VehiclesZuulFilter extends ZuulFilter {

	private final AuthService authService;

	@Autowired
	public VehiclesZuulFilter(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		Map<String, List<String>> params = convertParameterMap(context.getRequest().getParameterMap());
		params.put("user", List.of(authService.getUser().getName()));
		context.setRequestQueryParams(params);
		return null;
	}
	
	private Map<String, List<String>> convertParameterMap(Map<String, String[]> origMap) {
		Map<String, List<String>> newMap = new HashMap<>();
		for (Map.Entry<String, String[]> entry : origMap.entrySet()) {
			newMap.put(entry.getKey(), Arrays.asList(entry.getValue()));
		}
		return newMap;
	}
	
	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	@Override
	public String filterType() {
		return org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
	}
	
	@Override
	public int filterOrder() {
		return 2;
	}
}
