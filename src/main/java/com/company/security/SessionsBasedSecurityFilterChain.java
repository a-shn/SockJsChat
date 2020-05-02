package com.company.security;

import com.company.config.ApplicationContextConfig;
import com.company.models.User;
import com.company.repositories.SessionsRepository;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class SessionsBasedSecurityFilterChain implements Filter {
    private SessionsRepository sessionsRepository;
    private ApplicationContextConfig applicationContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        applicationContext = (ApplicationContextConfig) filterConfig.getServletContext().getAttribute("applicationContext");
        sessionsRepository = applicationContext.sessionsRepository();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            String path = httpServletRequest.getServletPath();
            Cookie[] cookies = httpServletRequest.getCookies();
            String sessionId = "";
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("sessionId")) {
                        sessionId = cookie.getValue();
                    }
                }
            }
            if (path.equals("/signin") || path.equals("/signup")) {
                Optional<User> optionalUser = sessionsRepository.findUserBySession(sessionId);
                if (optionalUser.isPresent()) {
                    httpServletResponse.sendRedirect("/chat");
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            } else {
                if (sessionId != null) {
                    Optional<User> optionalUser = sessionsRepository.findUserBySession(sessionId);
                    if (optionalUser.isPresent()) {
                        filterChain.doFilter(servletRequest, servletResponse);
                    } else {
                        httpServletResponse.sendRedirect("/signin");
                    }
                } else {
                    httpServletResponse.sendRedirect("/signin");
                }
            }
        }
    }

    @Override
    public void destroy() {
    }
}
