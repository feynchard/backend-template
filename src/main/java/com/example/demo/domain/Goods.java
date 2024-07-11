package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
@Table(name = "goods")
@Entity
public class Goods {

    @Id
    @Column(name = "_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "cr_user")
    private UUID crUser;

    @Column(name = "cr_datetime")
    private Timestamp crDatetime;

    @Column(name = "up_user")
    private UUID upUser;

    @Column(name = "up_datetime")
    private Timestamp upDatetime;

    public void setCreateInfo(UUID userId) {
        this.crUser = userId;
        this.crDatetime = new Timestamp(new Date().getTime());
        this.upUser = userId;
        this.upDatetime = new Timestamp(new Date().getTime());
    }

    public void setUpdateInfo(UUID userId) {
        this.upUser = userId;
        this.upDatetime = new Timestamp(new Date().getTime());
    }
}
