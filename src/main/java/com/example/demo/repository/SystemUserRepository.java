package com.example.demo.repository;

import com.example.demo.domain.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface SystemUserRepository extends JpaRepository<SystemUser, UUID> {

    public Optional<SystemUser> findByAccount(String account);

}