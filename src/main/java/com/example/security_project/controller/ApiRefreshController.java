package com.example.security_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.security_project.exception.CustomJWTException;
import com.example.security_project.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ApiRefreshController {    
    
    @GetMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, @RequestParam("refreshToken") String refreshToken) {
        
        if (refreshToken == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_AUTH");
        }

        String accessToken = authHeader.substring(7);

        // accessToken이 만료되지 않은 경우
        if (!checkExpiration(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // accessToken이 만료된 경우, refreshToken을 검증하고 검증이 된 경우 새로운 accessToken 생성
        Map<String, Object> claims = JwtUtil.validateToken(refreshToken);

        log.info("claims : {} ", claims);        

        // 새로운 AccessToken 생성
        String newAccessToken = JwtUtil.generateToken(claims, 10); // 10분

        log.info("expire : {} ", claims.get("exp")); // 만료시간

        String newRefreshToken = checkTime((Integer)claims.get("exp")) == true ? JwtUtil.generateToken(claims, 16*24) : refreshToken;  

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);

    }


    // accessToken의 만료 여부 확인
    private boolean checkExpiration(String accessToken) {

        try {
            JwtUtil.validateToken(accessToken);
        } catch (Exception e) {
            if (e.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }

    // refresh token 만료 시간이 1시간 미만인지 여부 확인
    // 단, 만료시간의 단위는 초
    private boolean checkTime(Integer exp){

        // 초를 밀리초로 변환해서 Date 객체로 생성
        Date expDate = new Date(exp * 1000L);

        // 현재 시간과의 차이 계산(ms)
        long gap = expDate.getTime() - System.currentTimeMillis();

        // 밀리초를 분으로 변환
        long leftTime = gap / (1000 * 60);

        // 60분 미만인 경우
        return leftTime < 60; 
    }
    
}
