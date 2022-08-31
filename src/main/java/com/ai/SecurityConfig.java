package com.ai;

import com.ai.entity.User.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.Resource;

@EnableWebSecurity
public class SecurityConfig {

    @Resource(name = "MyUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/"));
        http.logout(logout -> logout.logoutUrl("/logout").invalidateHttpSession(true).logoutSuccessUrl("/"))
                .rememberMe().key("remember-me").tokenValiditySeconds(172800).rememberMeCookieName("remember-me");
        http.exceptionHandling().accessDeniedPage("/denied-page");
        http.authorizeHttpRequests(auth -> auth
                .mvcMatchers("/login", "/resources/**").permitAll()
                .mvcMatchers("/admin/**").hasAuthority(Role.Admin.name())
                .mvcMatchers("/teacher/**").hasAuthority(Role.Teacher.name())
                .mvcMatchers("/student/**").hasAuthority(Role.Student.name()).anyRequest().authenticated());
        http.exceptionHandling().accessDeniedPage("/denied-page");
        return http.build();
    }

}
