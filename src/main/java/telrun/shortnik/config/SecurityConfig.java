package telrun.shortnik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return
                http
//                        .sessionManagement(x -> x
//                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                                .invalidSessionUrl("/login")
//                                .sessionFixation().migrateSession()
//                                .maximumSessions(1)
//                                .maxSessionsPreventsLogin(false)
//                        )
                        .authorizeHttpRequests(x -> x
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/main").authenticated()
//                                .requestMatchers("/url").authenticated()
                                .requestMatchers("/").authenticated()
                                .anyRequest().permitAll())
                        .csrf(csrf -> csrf.disable())
//                        .httpBasic(Customizer.withDefaults())
                        .formLogin(login -> login
                                .loginPage("/login")
                                .successForwardUrl("/main"))
//                                .successHandler((request, response, authentication) -> {
//                                    response.sendRedirect("/main");
//                                }))
                        .build();
    }
//                .formLogin(withDefaults()) // Настраиваем страницу входа
//                .logout(withDefaults()) // Настраиваем страницу выхода

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
