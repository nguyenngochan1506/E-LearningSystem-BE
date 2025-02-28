package vn.edu.ngochandev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // create spring web security
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->{
                    authorizeRequests.requestMatchers("/**")
                            .permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(sessionManagement ->{
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

        return http.build();
    }

    //config spring web configure
    @Bean
    public WebSecurityCustomizer ignoreResources(){
        return webSecurity -> webSecurity.ignoring()
                .requestMatchers("/actuator/**", "/v3/**", "/webjars/**", "/swagger-ui*/**", "/swagger-ui*/*swagger-initializer.js", "/favicon.ico");
    }

    // create bean passwordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
