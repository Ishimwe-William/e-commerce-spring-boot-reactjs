package com.bunsen.ecommerce.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.bunsen.ecommerce.model.AppUser;
import com.bunsen.ecommerce.model.dao.UserDao;
import com.bunsen.ecommerce.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTRequestFilter.class);
    private final JWTService jwtService;
    private final UserDao userDao;

    public JWTRequestFilter(JWTService jwtService, UserDao UserDao) {
        this.jwtService = jwtService;
        this.userDao = UserDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            try {
                String username = jwtService.getUsername(token);
                if (!jwtService.isTokenExpired(token)) {
                    Optional<AppUser> opUser = userDao.findByUsernameIgnoreCase(username);
                    if (opUser.isPresent()) {
                        AppUser user = opUser.get();
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    logger.warn("Token expired!");

                    Optional<AppUser> opUser = userDao.findByUsernameIgnoreCase(username);
                    if (opUser.isPresent()) {
                        AppUser user = opUser.get();
                        user.setTokenExpired(true);
                        userDao.save(user);
                    }
                }
            } catch (JWTDecodeException ignored) {

            }
        }
        filterChain.doFilter(request, response);
    }
}
