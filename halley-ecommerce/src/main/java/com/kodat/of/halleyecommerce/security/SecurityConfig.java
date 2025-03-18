package com.kodat.of.halleyecommerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.
                                requestMatchers(

                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "users/reset-password",
                                        "users/reset-password/request",
                                        "cart/**",
                                        "categories/tree",
                                        "categories/findAll",
                                        "categories/getMainCategories",
                                        "categories/category-paths/{categoryId}",
                                        "users/reset-password/request",
                                        "users/reset-password",
                                        "users/validate-token",
                                        "order",
                                        "order/orderSummary/**",
                                        "showcase/**",
                                        "auth/register",
                                        "auth/login",
                                        "auth/admin/login"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "products/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"brands/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"categories/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"blog/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"rooms/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"designs/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"colours/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
