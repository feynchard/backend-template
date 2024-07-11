package com.example.demo.config;

import com.example.demo.filter.JwtTokenFilter;
import com.example.demo.handler.CustomAuthenticationFailureHandler;
import com.example.demo.service.SystemUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final SystemUserService systemUserService;

    private final JwtTokenFilter jwtTokenFilter;

    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(SystemUserService systemUserService, JwtTokenFilter jwtTokenFilter, BCryptPasswordEncoder passwordEncoder) {
        this.systemUserService = systemUserService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.passwordEncoder = passwordEncoder;
    }


    // Used by Spring Security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // @Bean
    // public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

    //     AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    //     DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
    //     daoAuthenticationProvider.setUserDetailsService(account -> systemUserService
    //             .findByAccount(account)
    //             .orElseThrow(
    //                     () -> new UsernameNotFoundException(
    //                             String.format("User: %s, not found", account)
    //                     )
    //             ));

    //     authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider);

    //     return authenticationManagerBuilder.build();

    // }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(account -> systemUserService
                .findByAccount(account)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                String.format("User: %s, not found", account)
                        )
                ));
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

       http.csrf(csrf -> csrf.disable());

        // Set session management to stateless
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

         /* // Set unauthorized requests exception handler
        http = http
            .exceptionHandling()
            .authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        ex.getMessage()
                    );
                }
            )
            .and(); */

        // Set permissions on endpoints
        http.authorizeRequests(authorize -> authorize
                // Our public endpoints
                .requestMatchers("/api/pub/**",
                        "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/priv/SystemUser/**").permitAll()
                .requestMatchers(HttpMethod.POST,
                        "/api/priv/systemUser/register",
                        "/api/priv/systemUser/login").permitAll()
                // Our private endpoints
                .anyRequest().authenticated())
            ;

        // Add JWT token filter
        http.addFilterBefore(
            jwtTokenFilter,
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}
