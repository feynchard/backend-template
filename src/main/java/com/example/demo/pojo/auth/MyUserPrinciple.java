package com.example.demo.pojo.auth;

import com.example.demo.domain.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserPrinciple implements UserDetails {

    private SystemUser user;

    public MyUserPrinciple(SystemUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount();
    }

    public String getName() {
        return user.getName();
    }

}
