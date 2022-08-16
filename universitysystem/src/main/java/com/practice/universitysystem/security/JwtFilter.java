package com.practice.universitysystem.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.practice.universitysystem.security.service.UserDetailsServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImp userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtFilter(UserDetailsServiceImp userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeder = request.getHeader("Authorization");

        if (authHeder != null && !authHeder.isBlank() && authHeder.startsWith("Bearer")) {

            String jwt = authHeder.substring(7);

            if (jwt.isBlank()) {

                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token at bearer header");

            } else {
                try {
                    String email = jwtUtil.validateAndRetrieve(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

                } catch (JWTVerificationException e){
                    log.error(e.getLocalizedMessage());
                }
            }
        }

        filterChain.doFilter(request, response);

    }
}
