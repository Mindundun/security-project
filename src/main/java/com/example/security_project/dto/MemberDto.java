package com.example.security_project.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.ToString;

@ToString
public class MemberDto extends User{
    
    private String email;

    private String password;

    private String nickName;

    private List<String> roleNames= new ArrayList<>();

    // constructor method
    public MemberDto(String email, String password, String nickName, List<String> roleNames) {
        super(email, password, roleNames.stream()
            .map(roleName -> new SimpleGrantedAuthority("ROLE_"+roleName))
            .collect(Collectors.toList()));

        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.roleNames = roleNames;
    }

}
