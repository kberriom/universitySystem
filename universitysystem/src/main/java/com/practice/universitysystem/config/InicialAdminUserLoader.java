package com.practice.universitysystem.config;

import com.practice.universitysystem.model.users.admin.Admin;
import com.practice.universitysystem.repository.users.admin.AdminRepository;
import com.practice.universitysystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin_name}")
    private String adminName;
    @Value("${admin_password}")
    private String password;
    @Value("${admin_mail_domain}")
    private String domain;

    public void run(ApplicationArguments args) {
        if (adminRepository.count() != 0) {
            return;
        }
        Admin admin = new Admin();
        admin.setName(adminName);
        admin.setLastName(adminName);
        admin.setGovernmentId("0");
        admin.setEmail(adminName + domain);
        admin.setMobilePhone("0");
        admin.setLandPhone("0");
        admin.setBirthdate(new Date());
        admin.setUsername(adminName);
        admin.setUserPassword(authService.getEncodedPassword(password));
        admin.setEnrollmentDate(new Date());
        adminRepository.save(admin);
    }
}
