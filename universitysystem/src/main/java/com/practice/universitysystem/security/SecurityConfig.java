package com.practice.universitysystem.security;

import com.practice.universitysystem.security.service.UserDetailsServiceImp;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsServiceImp;

    private final JwtFilter jwtFilter;

    private final Boolean swaggerEnabled;

    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp, JwtFilter jwtFilter, @Value("#{new Boolean('${swagger_enabled}')}") Boolean swaggerEnabled) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtFilter = jwtFilter;
        this.swaggerEnabled = swaggerEnabled;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authzConfigurer -> {
            authzConfigurer.requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/curriculum/**").permitAll()
                    .requestMatchers("/subject/**").permitAll();
            if (swaggerEnabled) {
                authzConfigurer.requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/**").permitAll()
                        .requestMatchers("/graphiql/**").permitAll();
            }
            authzConfigurer.requestMatchers("/**").authenticated();
        });
        http.exceptionHandling(exceptionConfigurer -> exceptionConfigurer.authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED")));
        http.sessionManagement(sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsServiceImp);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
