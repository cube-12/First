package com.example.librarysystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("配置安全过滤器链...");

        http
                .authorizeHttpRequests((requests) -> {
                    logger.info("配置请求授权规则...");
                    requests
                            .requestMatchers(
                                    "/",
                                    "/index",
                                    "/user/login",
                                    "/user/register",
                                    "/error",
                                    "/access-denied",
                                    "/css/**",
                                    "/js/**",
                                    "/images/**"
                            ).permitAll()
                            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                            .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                            .requestMatchers("/api/user/**").authenticated()
                            .requestMatchers("/books/**").authenticated()
                            .requestMatchers("/borrow/**").authenticated()
                            .requestMatchers("/user/center").authenticated()
                            .anyRequest().authenticated();
                })
                .formLogin((form) -> {
                    logger.info("配置表单登录...");
                    form
                            .loginPage("/user/login")
                            .loginProcessingUrl("/perform_login")
                            .failureUrl("/user/login?error=true")
                            .successHandler((request, response, authentication) -> {
                                logger.info("用户 {} 登录成功", authentication.getName());
                                logger.info("用户权限: {}", authentication.getAuthorities());

                                boolean isAdmin = authentication.getAuthorities().stream()
                                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

                                if (isAdmin) {
                                    response.sendRedirect("/admin/books");
                                } else {
                                    response.sendRedirect("/books");
                                }
                            })
                            .permitAll();
                })
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied"))
                .logout((logout) -> {
                    logger.info("配置注销功能...");
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/user/login?logout")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll();
                })
                .authenticationProvider(authenticationProvider())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/perform_login", "/admin/**"));

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
