////package com.book.security;
////
////import java.util.List;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.web.SecurityFilterChain;
////import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
////import org.springframework.web.cors.CorsConfiguration;
////import org.springframework.web.cors.CorsConfigurationSource;
////import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
////
////import jakarta.annotation.PostConstruct;
////
////@Configuration
////@EnableMethodSecurity
////public class SecurityConfig {
////
////    private final HeaderAuthFilter headerAuthFilter;
////
////    public SecurityConfig(HeaderAuthFilter headerAuthFilter) {
////        this.headerAuthFilter = headerAuthFilter;
////    }
////    @PostConstruct
////    public void loaded() {
////        System.out.println(">>> SECURITY CONFIG LOADED SUCCESSFULLY <<<");
////    }
////
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////
////        http
////            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ ADD THIS LINE
////            .csrf(csrf -> csrf.disable())
////            .authorizeHttpRequests(auth -> auth
////                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() // ✅ IMPORTANT
////                .anyRequest().authenticated()
////            )
////            .sessionManagement(sess -> sess
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////            )
////            .addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class)
////            .httpBasic(basic -> basic.disable())
////            .formLogin(form -> form.disable());
////
////        return http.build();
////    }
////
////    
//////    @Bean
//////    public CorsConfigurationSource corsConfigurationSource() {
//////        CorsConfiguration config = new CorsConfiguration();
//////        config.setAllowedOrigins(List.of("http://localhost:4200"));
//////        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//////        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//////        config.setAllowCredentials(true);
//////
//////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//////        source.registerCorsConfiguration("/**", config);
//////        return source;
//////    }
////    
////
////}
package com.book.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import jakarta.annotation.PostConstruct;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    private final HeaderAuthFilter headerAuthFilter;
//
//    public SecurityConfig(HeaderAuthFilter headerAuthFilter) {
//        this.headerAuthFilter = headerAuthFilter;
//    }
//
//    @PostConstruct
//    public void loaded() {
//        System.out.println(">>> API GATEWAY SECURITY LOADED <<<");
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//            // ✅ CORS ONLY HERE
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//
//            .csrf(csrf -> csrf.disable())
//
//            .authorizeHttpRequests(auth -> auth
//                // allow browser preflight
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                // public APIs (example)
//                .requestMatchers("/auth/**").permitAll()
//
//                // all other APIs require auth
//                .anyRequest().authenticated()
//            )
//
//            .sessionManagement(sess ->
//                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//
//            // custom header-based auth
//            .addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class)
//
//            .httpBasic(basic -> basic.disable())
//            .formLogin(form -> form.disable());
//
//        return http.build();
//    }
//
//    // ✅ SINGLE CORS CONFIG (ONLY PLACE)
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:4200"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-USER", "X-ROLE"));
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source =
//            new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
//}
//
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}


