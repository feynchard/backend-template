package com.example.demo.filter;

import com.example.demo.service.SystemUserService;
import com.example.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final SystemUserService systemUserService;

    public JwtTokenFilter(JwtService jwtService, SystemUserService systemUserService) {
        this.jwtService = jwtService;
        this.systemUserService = systemUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
//        log.info("Enter JwtTokenFilter");

        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {

//            log.error("Bearer token can't leave with empty.");
//
//            log.info("Leaver JwtTokenFilter");

            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtService.validate(token)) {

//            log.error("invalidate token is used.");
//
//            log.info("Leaver JwtTokenFilter");

            chain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        UserDetails userDetails = systemUserService
            .findByAccount(jwtService.getAccount(token)).orElse(null);

        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                    List.of() : userDetails.getAuthorities()
            );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

//        log.info("authencated.");
//
//        log.info("Leaver JwtTokenFilter");

        chain.doFilter(request, response);
    }

}