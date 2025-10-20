package com.example.security_project.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.security_project.domain.Member;
import com.example.security_project.domain.MemberRole;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.*;

@Slf4j 
@SpringBootTest // CustomSecurityCinfig의 비밀번호 암호화를 사용하기 위해 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testSave(){
        List<Member> members = new ArrayList<>();

        for(int i = 1; i <= 10; i++){
            Member member =  Member.builder()
                                .email("user"+i+"@naver.com")
                                .password(passwordEncoder.encode("1111"))
                                .nickName("user"+i)
                                .build();
            
            member.addRole(MemberRole.USER); 
            
            if(i >= 5){
                member.addRole(MemberRole.MANAGER); 
            } 
            
            if(i >= 8){ 
                member.addRole(MemberRole.ADMIN); 
            }

            members.add(member);
        }

        // memberRepository.saveAll((List<Member>) members);
        memberRepository.saveAll(members);

    }

    @Test
    void testGetWithRoles() {

        // Given
        String email = "user7@naver.com";

        // When
        Member member = memberRepository.getWithRoles(email);


        // Then
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getRoles()).hasSize(2);

        member.getRoles().forEach(role -> { // MemberRole
            System.out.println(role.name());
        });

    }
}
