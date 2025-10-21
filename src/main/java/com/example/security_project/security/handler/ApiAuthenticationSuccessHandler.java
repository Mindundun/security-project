package com.example.security_project.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.security_project.dto.MemberDto;
import com.example.security_project.util.JwtUtil;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 인증에 성공한 경우
@Slf4j
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {        
        
        log.info("-------------------------------authentication : {}", authentication);

        // email
        MemberDto memberDto = (MemberDto)authentication.getPrincipal();
        // String email = memberDto.getEmail();

        // // content-Type 설정
        // // 아래의 타입은 application/json 혹은 text/html을 사용해야함
        // response.setContentType("text/html; charset=utf-8");
        
        // log.info("-------------------------------Success email : {}", email);

        // PrintWriter pw = response.getWriter();
        // pw.println("<h1>" + email + "님, 로그인에 성공하셨습니다." + "</h1>");
        // pw.close();

        // claims
        Map<String, Object> claims = memberDto.getClaims();
        
        // Access Token, Refresh Token 생성
        String accessToken = JwtUtil.generateToken(claims, 10);     // 10분

        String refreashToken = JwtUtil.generateToken(claims, 60 * 24);  // 1일

        claims.put("accessToken", accessToken);
        claims.put("refreashToken", refreashToken);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=utf-8");
        PrintWriter pw = response.getWriter();
        pw.println(jsonStr);
        pw.close();
        
    }
    
    
}
