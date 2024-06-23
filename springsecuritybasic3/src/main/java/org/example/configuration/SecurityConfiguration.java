package org.example.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user").authenticated()
                .antMatchers("/statement").authenticated()
                .anyRequest().permitAll()       //if you use this line then
                // it all request endpoint are allowed to access without
                // authentication except the above two provided specifically
                // in antMAcher.Also if you do not provide this line then
                // alsothe result will be same i.e it will only authenticate
                // the specified endpoints.
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }
}
