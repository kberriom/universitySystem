package com.practice.universitysystem.repository.users;

import com.practice.universitysystem.model.users.UniversityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityUserRepository extends JpaRepository<UniversityUser, Long> {

    public Optional<UniversityUser> findByEmail(String email);
}
