package com.example.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;


// BCryptPasswordEncoder 객체를 직접 new로 생성하는 방식보다는 PasswordEncoder 객체를 빈으로 등록해서 사용하는 것이 좋다. 
// 왜냐하면 암호화 방식을 변경하면 BCryptPasswordEncoder를 사용한 모든 프로그램을 일일이 찾아다니며 수정해야 하기 때문이다.
// TODO: PasswordEncoder 빈을 만드는 가장 쉬운 방법은 @Configuration이 적용된 SecurityConfig.java 파일에 @Bean 메서드를 새로 추가
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// 인증 처리 
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

// SecurityConfig에 적용한 @EnableMethodSecurity 애너테이션의 prePostEnabled = true는 QuestionController와 AnswerController에서 
// 로그인 여부를 판별할 때 사용한 @PreAuthorize 애너테이션을 사용하기 위해 반드시 필요한 설정
@EnableMethodSecurity(prePostEnabled = true) 
@Configuration // 이 파일이 스프링의 환경 설정 파일임을 의미하는 애너테이션
@EnableWebSecurity // 이 파일이 스프링의 환경 설정 파일임을 의미하는 애너테이션
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            .csrf((csrf) -> csrf
                    .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
            .headers((headers) -> headers
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            // 로그인 페이지의 URL은 /user/login이고 로그인 성공 시에 이동할 페이지는 루트 URL(/)임을 의미
            .formLogin((formLogin) -> formLogin  
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/"))
            // 로그아웃 처리 
            .logout((logout) -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true))
        ;
        return http.build();
    }
    
    // passwordencoder 메서드 추가 
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //인증처리 메서드 추가 
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
