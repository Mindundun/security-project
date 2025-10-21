package com.example.security_project.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.security_project.security.filter.JWTCheckFilter;
import com.example.security_project.security.handler.ApiAuthenticationFailureHandler;
import com.example.security_project.security.handler.ApiAuthenticationSuccessHandler;
import com.example.security_project.security.handler.CustomAccessDeniedHandler;

import lombok.extern.slf4j.Slf4j;

// Java Config Class
@Slf4j
@Configuration
@EnableWebSecurity // Security를 활성화 하는 annotation
@EnableMethodSecurity // Method 레벨에서 접근 제한
public class CustomSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    CustomSecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }
    
    // SecurityFilterChain을 Bean으로 등록해야함
    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        log.info("---------------------------------------------SecurityFilterChain");

        // 1. CORS 설정 활성화
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()); // DI
        });

        // 2. 세션 비활성화
        http.sessionManagement(sessionConfig -> 
            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 3. CSRF 비활성화
        http.csrf(csrf -> csrf.disable());
        
        // 폼 기반 로그인 요청 처리
        http.formLogin(config -> {
            config.loginProcessingUrl("/api/v1/members/login"); // POST
            config.usernameParameter("email");
            // config.passwordParameter("password");
            config.successHandler(new ApiAuthenticationSuccessHandler());
            config.failureHandler(new ApiAuthenticationFailureHandler());
            }
            
        );
        
        // JWTCheckFilter를 먼저 처리 후 처리 결과에 따라 UsernamePasswordAuthenticationFilter.class 처리
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        // 인가(Authorization)
        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler()); // 권한 없는 상태에서 API 요청 시 실행

        });

        return http.build();
    }

    // CORS 설정 활성화
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // 모든 도메인에 대해 허용
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        // 어떤 메소드를 허용할 것인지 
        configuration.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));

        // 브라우저에서 자바스크립트로 응답 헤더에 접근하고 싶을 때
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        // 클라우드 인증정보를 요청헤더에 포함할지, 하지만 모든 도메인에 허용하는 경우 아래의 처리는 되지 않음
        // configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 패스워드 암호화(BCrypt 해싱 알고리즘)
        return new BCryptPasswordEncoder();
    }
}