package com.example.security_project.util;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.example.security_project.exception.CustomJWTException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

/**
 * 할 일
 * 1. JWT 토큰 생성 : 비밀키 생성(signature내에서 필요), payload, header를 사용해서 해시 함수로 서명
 * 2. JWT 토큰 검증
 */
public class JwtUtil {
    
    // 키 값은 30자 이상 사용할 것!!
    private static String key ="12345678900x51f1w6f8g4w2f1f884eg5w5e1r816d8hhthw";

    
    // 1. JWT 토큰 생성
    public static String generateToken(Map<String, Object> claims, int min) {

        SecretKey secretKey = null;

        try {
            secretKey = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("utf-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        // ZonedDateTime은 UTC 사용하는데 "Asia/Seoul"로 바뀜
        return Jwts.builder()
                    .setHeader(Map.of("typ", "JWT"))
                    .setClaims(claims)
                    .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                    .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                    .signWith(secretKey)
                    .compact(); // 🔥 최종적으로 JWT 문자열 생성

      
    }

    // JWT Token 검증
    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claims = null;

        try {
            // 비밀키 : 토큰 위변조 확인
            SecretKey secretKey = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("utf-8"));

            claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

        } catch (MalformedJwtException malformedJwtException) { 
            throw new CustomJWTException("Malformed");      // JWT 형식이 잘 못 되었을 때 발생
        } catch (ExpiredJwtException expiredJwtException) { 
            throw new CustomJWTException("Expired");        // JWT 토큰이 만료 시 발생
        } catch (InvalidClaimException invalidClaimException) { 
            throw new CustomJWTException("InvalidClaim");   // claim 정보가 유효하지 않은 경우 발생
        } catch (JwtException jwtException) {
            throw new CustomJWTException("Jwt Error");      // 서명 검증에 실패한 경우 발생
        } catch (Exception exception) {
            throw new CustomJWTException("Error");          
        }

        return claims;
    }

}
