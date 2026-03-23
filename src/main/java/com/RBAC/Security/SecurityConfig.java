package com.RBAC.Security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("dataSource")
    private DataSource securityDataSource;

//    @Autowired
//    private CustomSuccessHandler successHandler;
    @Autowired
	AuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(securityDataSource)
            .passwordEncoder(getPasswordEncoder())
            .usersByUsernameQuery("select username, password, enabled from logininformation where username = ?")
            .authoritiesByUsernameQuery("select username, role from logininformation where username = ?");
    }

    @Bean(name = "myPasswordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delPasswordEncoder;
    }
    


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .antMatchers("/", "/**/*.js", "/**/*.css", "/**/*.jpg", "/employee_registration", "/forgot_password", "/changepassword","/emp_reg", "/reg_success",
                           "/reset_password", "/**/*.png", "/**/*.svg", "/**/*.jpeg", "/uploads/**", "/ManagerDash").permitAll()  // Added /uploads/**
                    
                    .antMatchers("/profile").hasAnyAuthority("USER", "MANAGER", "ADMIN")
                    .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                    .antMatchers("/user/**").hasAnyAuthority("USER")
                    .antMatchers("/manager/**").hasAnyAuthority("MANAGER")
                    
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(successHandler)
            )
//            .logout(logout ->
//                logout
//                    .logoutUrl("/logout") // Specifies the logout URL
//                    .logoutSuccessUrl("/login") // Redirect to login page after logout
//                    .invalidateHttpSession(true) // Invalidate the HTTP session
//                    .deleteCookies("JSESSIONID") // Delete the JSESSIONID cookie
//                    .permitAll() // Allow all users to access the logout URL
//            )
            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .accessDeniedPage("/access-denied")
            )
            .headers(headers ->
                headers
                    .frameOptions().disable()
            )
            .sessionManagement(sessionManagement ->
                sessionManagement
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                    .expiredUrl("/login?invalid-session=true")
            );
        http.logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
		.logoutSuccessUrl("/login?logout").permitAll();
    }
}
