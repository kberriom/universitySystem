package com.practice.universitysystem.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.practice.universitysystem.security.service.UserDetailsServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImp userDetailsService;
    private final JwtUtil jwtUtil;
    private final RequestMatcher requestMatcher;

    @Autowired
    public JwtFilter(UserDetailsServiceImp userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        final ArrayList<RequestMatcher> requestMatcherIgnoredJWTList = new ArrayList<>();
        requestMatcherIgnoredJWTList.add(new AntPathRequestMatcher("/auth/login"));
        requestMatcher = new OrRequestMatcher(requestMatcherIgnoredJWTList);
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }
}
