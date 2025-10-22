package com.example.security_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.security_project.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{

    @Query("SELECT a FROM Member AS a JOIN FETCH a.roles WHERE a.email = :email")
    Member getWithRoles(@Param("email") String email);
    
}