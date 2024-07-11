package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
@Table(name = "system_user")
@Entity
public class SystemUser {

    @Id
    @Column(name = "_id")
    private UUID id;

    private String account;

    private String password;

    private String name;

}