package com.practice.universitysystem.service;

import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UniversityUserRepository universityUserRepository;

    public String getAuthUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * @param password raw password
     */
    public String authAndGenerateJwt(String email, String password) throws AuthenticationException {
        usernamePasswordAuthentication(email, password);
        return jwtUtil.generateToken(email);
    }

    /**
     * @param password raw password
     */
    public void usernamePasswordAuthentication(String email, String password) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        authManager.authenticate(authenticationToken);
    }

    public String changePassword(String email, String newPassword) throws NoSuchElementException {
        UniversityUser user = universityUserRepository.findByEmail(email).orElseThrow();
        String encodedPassword = getEncodedPassword(newPassword);
        user.setUserPassword(encodedPassword);
        universityUserRepository.save(user);
        return encodedPassword;
    }

    public String getEncodedPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
