package com.example.security_project.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 인증에 실패한 경우
@Slf4j
public class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        
        // // content-Type 설정
        // response.setContentType("text/html; charset=utf-8");

        // PrintWriter pw = response.getWriter();
        // pw.println("<h1>" + "Error : " + exception.getMessage() + "</h1>");
        // pw.close();

        log.error("ApiAuthenticationFailureHandler Error : {}", exception.getMessage());

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("error", "ERROR_LOGIN"));

        response.setContentType("application/json; charset=UTF-8");

        PrintWriter pw = response.getWriter();
        pw.println(jsonStr);
        pw.close();
    }

    
}
