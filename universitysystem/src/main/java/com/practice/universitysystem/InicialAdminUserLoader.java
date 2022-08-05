package com.practice.universitysystem;

import com.practice.universitysystem.model.users.admin.Admin;
import com.practice.universitysystem.repository.users.admin.AdminRepository;
import com.practice.universitysystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InicialAdminUserLoader implements ApplicationRunner {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AuthService authService;

    public void run(ApplicationArguments args) {
        String adminName = "admin";
        Admin admin = new Admin();
        admin.setName(adminName);
        admin.setLastName(adminName);
        admin.setGovernmentId("0");
        admin.setEmail(adminName + "@universitySystem.com");
        admin.setMobilePhone("0");
        admin.setLandPhone("0");
        admin.setBirthdate(new Date());
        admin.setUsername(adminName);
        admin.setUserPassword(authService.getEncodedPassword("admin"));
        admin.setEnrollmentDate(new Date());
        adminRepository.save(admin);
    }
}
