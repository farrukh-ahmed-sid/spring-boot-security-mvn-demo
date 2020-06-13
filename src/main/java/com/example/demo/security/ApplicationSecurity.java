package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

import static com.example.demo.security.Permissions.*;
import static com.example.demo.security.Roles.ADMIN;
import static com.example.demo.security.Roles.STUDENT;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoderConfig passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()
                .antMatchers("/api/v1/students/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.POST, "/api/v1/management/**").hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/api/v1/management/**").hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/api/v1/management/**").hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/api/v1/management/**").hasAnyRole(STUDENT.name(), ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                //.httpBasic();
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/welcome", true)
                    .usernameParameter("username")
                    .passwordParameter("password")
                .and()
                //.rememberMe();
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                    .rememberMeParameter("rememberme")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me", "XSRF-TOKEN")
                    .logoutSuccessUrl("/login");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService(){
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.passwordEncoder().encode("admin"))
                //.roles(ADMIN.name())
                .authorities(ADMIN.getSimpleGrantedAuthorities())
                .build();

        UserDetails student = User.builder()
                .username("student")
                .password(passwordEncoder.passwordEncoder().encode("student"))
                //.roles(STUDENT.name())
                .authorities(STUDENT.getSimpleGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(admin, student);
    }

}
