package com.example.security_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.security_project.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
    
}
