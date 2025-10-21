package com.example.security_project.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.security_project.dto.MemberDto;
import com.example.security_project.util.JwtUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// JWT Token 검증
@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter{

    // 필터 적용 여부 
    // return 값에 따라 ture : 필터 적용 X, false : 필터 적용O
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        
        String path = request.getRequestURI();
        String method = request.getMethod();

        log.info("================uri : {}", path);
        log.info("================HTTP method : {}", method);

        // 추후 경로는 개발 시 추가..
        if (method.equalsIgnoreCase("OPTIONS") || path.equals("/api/v1/members/login")) {
            return true; // JWTCkeckFilter를 적용하지 않는다는 의미
        }
        
        return false; // JWTCkeckFilter를 적용한다는 의미
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        log.info("Authorization Header : {}", authHeader);

        try {
            // Bearer [JWT토큰문자열] 중 JWT 토큰 문자열만 추출하기 위함
            String accessToken = authHeader.substring(7); 

            Map<String, Object> claims = JwtUtil.validateToken(accessToken);

            log.info("claims : {}", claims);

            String email = (String)claims.get("email");
            String password = (String)claims.get("password");
            String nickName = (String)claims.get("nickName");
            List<String> roleNames = (List<String>)claims.get("roleNames");


            MemberDto memberDto = new MemberDto(email, password, nickName, roleNames);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDto, password, memberDto.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("error : {}", e.getMessage());

            Throwable cause = e.getCause();
            if(cause instanceof AccessDeniedException) {
                throw (AccessDeniedException) cause;
            } else {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

                response.setContentType("application/json; charset=utf-8");
                PrintWriter pw = response.getWriter();
                pw.println(jsonStr);
                pw.close();
            }
        }
        
    }

    
    
    
}
