package com.example.demo.service;

import com.example.demo.domain.SystemUser;
import com.example.demo.pojo.auth.MyUserPrinciple;
import com.example.demo.pojo.http.RegisterRequest;
import com.example.demo.repository.SystemUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public SystemUserService(SystemUserRepository systemUserRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.systemUserRepository = systemUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public Optional<UserDetails> findByAccount(String account) {
        Optional<SystemUser> systemUserOptional = systemUserRepository.findByAccount(account);

        SystemUser systemUser = systemUserOptional.orElse(null);

        UserDetails user = systemUser == null ? null : new MyUserPrinciple(systemUser);

        return Optional.ofNullable(user);

    }

    public void register(RegisterRequest request) {
        SystemUser systemUser = SystemUser.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .account(request.getAccount())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        systemUserRepository.save(systemUser);
    }

    public Optional<SystemUser> getSystemUserByToken(String token) {

        String account = jwtService.getAccount(token);

        return systemUserRepository.findByAccount(account);

    }
}