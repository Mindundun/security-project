package com.example.security_project.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 인증에 실패한 경우
public class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        
        // content-Type 설정
        response.setContentType("text/html; charset=utf-8");

        PrintWriter pw = response.getWriter();
        pw.println("<h1>" + "Error : " + exception.getMessage() + "</h1>");
        pw.close();
    }

    
}
