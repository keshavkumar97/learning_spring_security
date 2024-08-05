package org.example.configuration;

import org.example.configuration.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    JwtRequestFilter jwtRequestFilter;
//    @Autowired
//    DaoAuthenticationProvider daoAuthenticationProvider;

    //    what all requests are secured will be defined by the below method
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").hasAuthority("EMPLOYEE")
                .antMatchers("/statement").hasAuthority("MANAGER")
                .antMatchers("/user").hasAuthority("ADMIN")
                .antMatchers("/contactus").permitAll()
//                .anyRequest().authenticated()
                .and().csrf().disable()         //if application is not meant
                // to be used on browser and you are sure that only
                // non-browser like postman will hit your application then
                // disable the csrf, otherwise you will get 403 response
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic();
        http.addFilterBefore(jwtRequestFilter,
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder bEncoder() {
        return new BCryptPasswordEncoder();
    }

    //how the authentication will be done is defined in the below method like
    // in-memory authentication/jdbc authentication/JWT authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider()   used to provide custom
//        authentication provider
//        auth.authenticationProvider(daoAuthenticationProvider);
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(encoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
