package com.sydoruk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final  PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig (DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select email, password, 'true' from users where email=?")
                .authoritiesByUsernameQuery("select email, role from users where email=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/map", "/history","/invoice-list-paging/**","/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/reservation").hasAnyAuthority("ROLE_USER")
                .antMatchers("/", "/login","/registration").permitAll()
                .and()
                .formLogin().permitAll()
                .loginPage("/login")
                .loginProcessingUrl("/perform-login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .and()
                .csrf().disable();


        http.logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID"));
    }
}