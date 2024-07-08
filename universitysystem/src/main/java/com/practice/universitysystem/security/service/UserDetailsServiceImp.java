package com.practice.universitysystem.security.service;

import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    UniversityUserRepository userRepository;
    @Autowired
    UserRoleSelector userRoleSelector;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String role = userRoleSelector.getUserRoleByHierarchy(email);

        Optional<UniversityUser> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            throw new UsernameNotFoundException("Unable to find user using email");
        }
        UniversityUser universityUser = findUser.get();

        return new User(email, universityUser.getUserPassword(), Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
