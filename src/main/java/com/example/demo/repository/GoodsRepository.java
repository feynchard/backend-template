package com.example.demo.repository;

import com.example.demo.domain.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GoodsRepository extends JpaRepository<Goods, UUID> {
}
