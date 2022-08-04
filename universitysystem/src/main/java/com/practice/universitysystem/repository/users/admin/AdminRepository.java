package com.practice.universitysystem.repository.users.admin;

import com.practice.universitysystem.model.users.admin.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);

}
