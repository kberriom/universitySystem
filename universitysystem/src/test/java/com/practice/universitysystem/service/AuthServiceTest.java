package com.practice.universitysystem.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    AuthService authService;

    @Test
    void getAuthUserEmailTest() {
        when(authService.getAuthUserEmail()).thenReturn("user@university.com");

        assertEquals("user@university.com", authService.getAuthUserEmail());

        verify(authService).getAuthUserEmail();
    }

    @Test
    void authAndGenerateJwtTest() {
        when(authService.authAndGenerateJwt(anyString(), anyString())).thenReturn("JWT");

        assertEquals("JWT", authService.authAndGenerateJwt("user@university.com", "pass"));
    }

    @Test
    void changePasswordTest() {
        when(authService.changePassword(anyString(), anyString())).thenReturn("encodedPassword");

        assertEquals("encodedPassword", authService.changePassword("user@university.com", "new pass"));
    }

    @Test
    void getEncodedPasswordTest() {
        when(authService.getEncodedPassword(anyString())).thenReturn("encodedPassword");

        assertEquals("encodedPassword", authService.getEncodedPassword(anyString()));
    }
}
