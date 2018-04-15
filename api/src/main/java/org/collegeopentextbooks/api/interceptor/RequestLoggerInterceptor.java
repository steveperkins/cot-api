package org.collegeopentextbooks.api.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestLoggerInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG = Logger.getLogger(RequestLoggerInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		LOG.info("Request from " + getRemoteAddr(request) + " " + request.getMethod() + " " + request.getRequestURI() + getParameters(request));

		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,Object handler, Exception ex) throws Exception {
		if (ex != null){
	        LOG.error(ex);
	    }
	}
	
	private String getParameters(HttpServletRequest request) {
	    StringBuffer parameters = new StringBuffer();
	    Enumeration<?> requestParametersEnumeration = request.getParameterNames();
	    if (requestParametersEnumeration != null && requestParametersEnumeration.hasMoreElements()) {
	        parameters.append("?");
	    } else {
	    	return "";
	    }
	    while (requestParametersEnumeration.hasMoreElements()) {
	        if (parameters.length() > 1) {
	            parameters.append("&");
	        }
	        String curr = (String) requestParametersEnumeration.nextElement();
	        parameters.append(curr + "=").append(request.getParameter(curr));
	    }
	    return parameters.toString();
	}
	
	private String getRemoteAddr(HttpServletRequest request) {
	    String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
	    if (ipFromHeader != null && ipFromHeader.length() > 0) {
	        return ipFromHeader;
	    }
	    return request.getRemoteAddr();
	}
}
