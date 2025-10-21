package com.example.security_project.service;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.security_project.domain.Member;
import com.example.security_project.dto.MemberDto;
import com.example.security_project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Member member = memberRepository.getWithRoles(email);

        if (member == null){
            throw new RuntimeException(email + " Not found.");
        }

        // 인증된 사용자 정보를 담음
        MemberDto memberDto = new MemberDto(member.getEmail(), member.getPassword(), member.getNickName(), 
                                            member.getRoles().stream().map(role -> role.name()).collect(Collectors.toList()));
                                            
        log.info("memberDto : {}", memberDto.toString());

        return memberDto;
    }
    
}
