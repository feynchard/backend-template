package com.example.demo.web;

import com.example.demo.pojo.auth.MyUserPrinciple;
import com.example.demo.pojo.http.AuthRequest;
import com.example.demo.pojo.http.RegisterRequest;
import com.example.demo.pojo.http.UserVO;
import com.example.demo.service.JwtService;
import com.example.demo.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/priv/systemUser")
public class SystemUserApi {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final SystemUserService systemUserService;

    public SystemUserApi(AuthenticationManager authenticationManager, JwtService jwtService, SystemUserService systemUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.systemUserService = systemUserService;
    }


    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        systemUserService.register(request);

        return ResponseEntity.ok("success");
    }

    @PostMapping("login")
    public ResponseEntity<UserVO> login(@RequestBody AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getAccount(), request.getPassword()
                    )
                );

            MyUserPrinciple principal = (MyUserPrinciple) authenticate.getPrincipal();

            return ResponseEntity.ok()
                .header(
                    HttpHeaders.AUTHORIZATION,
                    jwtService.generateAccessToken(principal)
                )
                .body(UserVO.builder()
                        .account(request.getAccount())
                        .name(principal.getName())
                        .build());

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}