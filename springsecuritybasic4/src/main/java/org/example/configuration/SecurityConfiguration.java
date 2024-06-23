package org.example.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    //what all requests are secured will be defined by the below method
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/statement").authenticated()
                .antMatchers("/user").authenticated()
                .anyRequest().permitAll()
                .and().httpBasic()
                .and().formLogin();
    }
    //how the authentication will be done is defined in the below method like
    // in-memory authentication/jdbc authentication/JWT authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //as we have overridden this method and provided new username and
        // password os spring will treat this as a very new user and though
        // you have to provide role for each new user created either by using
        // role() or authorities() else you will get the below error message
        // Error creating bean with name 'springSecurityFilterChain'
        // defined in class path resource
        auth.inMemoryAuthentication()
                .withUser("kartik").password("kartik@123").authorities("admin").and()
                .withUser("Jamun").password("jamun@123").authorities("read").and()
                .withUser("shyam").password("shyam@123").authorities("read").and()
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
