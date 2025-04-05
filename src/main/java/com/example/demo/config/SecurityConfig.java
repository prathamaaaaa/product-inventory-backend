package com.example.demo.config;

import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;	

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/admin/register", "/admin/login","api/products/update/*","/auth/google",
            				"/api/products/all","/api/products/save-category","/api/products/save-subcategory",
            				"/api/products/categories/*","/api/products/subcategories/*","/api/stores/add",
            				"/api/stores/admin/*","/api/stores/*","/api/stores/product/*","/api/stores/download-csv/*",
            				"/admin/download-categories/*","/admin/download-subcategories/*","/admin/download-products/*",
            				"/api/products/toggle-active/*","/admin/list/*","api/products/*","/admin/request-delete/*",
            				"/admin/download-csv/*","/admin/confirm-delete/*","/api/stores/delete/*","/locale/*","/api/stores/add/",
            				"/api/products/cart","/api/products/cart/bulk","/api/products/cart/*", "/api/checkout/save",
            				"/api/products/save-product").permitAll()
            		.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

//(auth -> auth
//		.requestMatchers(
//				"/api/products/all",
//				"/api/products/update/*",
//				"/api/products/toggle-active/*",
//				"/api/products/list",
//				"/api/products/**",
//				"/auth/google",
//				"/admin/adminpanel",
//				"/admin/list/*",
//				"/admin/register"
//				).permitAll()
//		)