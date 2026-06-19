//package com.E_commerce.Config;
//
//import com.E_commerce.Config.JWT.JwtAuthenticationEntryPoint;
//import com.E_commerce.Config.JWT.JwtAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtFilter;
//    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint(authenticationEntryPoint)
//                )
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/auth/login",
//                                "/api/auth/register",
//                                "/api/auth/emailverify","/api/auth/otp/verify"
//                        ).permitAll()
//
//                        .requestMatchers("/api/brands/**").hasRole("ADMIN")
//                .requestMatchers("/api/categories/**").hasRole("ADMIN")
//                        .requestMatchers("/api/user/**").hasRole("USER")
//
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public CorsConfigurationSource corsConfigurationSource() {
////        CorsConfiguration configuration = new CorsConfiguration();
////
////
////        configuration.setAllowedOriginPatterns(List.of(
////                "http://localhost:*",
////                "http://192.168.1.40.*:3001"
////        ));
////
////        configuration.setAllowedMethods(List.of("*"));
////        configuration.setAllowedHeaders(List.of("*"));
////        configuration.setAllowCredentials(true);
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", configuration);
////        return source;
////    }
//
//
//}
//
package com.VillGo.Config;

import com.VillGo.Entity.Role;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.VillGo.Config.JWT.JwtAuthenticationEntryPoint;
import com.VillGo.Config.JWT.JwtAuthenticationFilter;
import org.springframework.http.HttpMethod;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})   // IMPORTANT
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        //  Allow preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/emailverify",
                                "/api/auth/otp/verify",
                                "/api/products/active"
                        ).permitAll()
                        .requestMatchers("/api/superauth/**").hasAnyRole("SUPER_ADMIN","ADMIN")
                        .requestMatchers("/api/auth/updateuser/**").hasAnyRole("USER","ADMIN","SUPER_ADMIN")
                        .requestMatchers("/api/brands/**").hasAnyRole("SUPER_ADMIN","ADMIN")
                        .requestMatchers("/api/categories/**").hasAnyRole("SUPER_ADMIN","ADMIN")
                        .requestMatchers("/api/products/**").hasAnyRole("SUPER_ADMIN","ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("USER","SUPER_ADMIN")
                        .requestMatchers("/api/wishlist/**").hasAnyRole("ADMIN","USER","SUPER_ADMIN")
                        .requestMatchers("/api/cart/**").hasAnyRole("ADMIN","USER","SUPER_ADMIN")
                        .requestMatchers("/api/orders/**").hasAnyRole("ADMIN","USER","SUPER_ADMIN")
                        .requestMatchers("/api/dashboard/**").hasRole("SUPER_ADMIN")
                        //.requestMatchers("/api/company/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://demovillgo.vercel.app"
        ));
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret
                )
        );
    }
    @Bean
    public org.springframework.boot.CommandLineRunner createAdmin(
            com.VillGo.Repository.UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String email = "admin@gmail.com";

            // If admin already exists → do nothing
            if (userRepository.findByEmail(email).isPresent()) {
                //System.out.println("Admin already exists");
                return;
            }

            com.VillGo.Entity.User admin = new com.VillGo.Entity.User();
            admin.setFullName("Admin");
            admin.setEmail(email);
            admin.setMobile("9999999999");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setDeleted(false);

            userRepository.save(admin);

            System.out.println("✅ Default Admin Created");
            System.out.println("Email: admin@gmail.com");
            System.out.println("Password: admin123");
        };
    }

}
