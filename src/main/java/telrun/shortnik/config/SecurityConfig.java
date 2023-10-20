package telrun.shortnik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .sessionManagement(x -> x
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .invalidSessionUrl("/login")
                                .sessionFixation().migrateSession()
                                .maximumSessions(2)
                                .maxSessionsPreventsLogin(false))
                        .authorizeHttpRequests(x -> x
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/main").authenticated()
                                .requestMatchers("/").authenticated()
                                .requestMatchers(HttpMethod.POST, "/url").authenticated()
                                .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/user/{username}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/user/{userId}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/url/delete/{shortUrl}").hasRole("ADMIN")
                                .anyRequest().permitAll())
                        .csrf(csrf -> csrf.disable())
                        .formLogin(login -> login
                                .loginPage("/login")
//                                .successForwardUrl("/main")) danger!!!
                                .successHandler((request, response, authentication) -> {
                                    response.sendRedirect("/main");
                                }))
//                        .httpBasic(Customizer.withDefaults())
                        .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
