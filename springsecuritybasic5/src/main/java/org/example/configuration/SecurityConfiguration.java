package org.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private BCryptPasswordEncoder encoder;

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
                .httpBasic()
                .and().formLogin();
    }

    @Bean
    public BCryptPasswordEncoder bEncoder() {
        return new BCryptPasswordEncoder();
    }

    //how the authentication will be done is defined in the below method like
    // in-memory authentication/jdbc authentication/JWT authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, isactive " +  //username, password,isactive/enabled is almost mandatory for jdbc authentication
                        "from " +
                        "public.user " +
                        "where username=?")
                .authoritiesByUsernameQuery("select username, role as " +
                        "authority" +
                        " from " +
                        "public.user " +
                        "where username=?")
                .passwordEncoder(encoder);
    }
}
