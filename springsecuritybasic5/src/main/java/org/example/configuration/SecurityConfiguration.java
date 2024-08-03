package org.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    private final BCryptPasswordEncoder encoder;

    //in constructor injection @autowired is optional because when
    // dependencies passed through constructor spring automatically
    // understands and perform dependency injection but still if you provide
    // @autowired annotation it is good for other developer to understand
    // that here constructor is used for dependency injection. this
    // annotation can also be used in one more case when you have multiple
    // constructor then to specify that which constructor is being used for
    // dependency injection it is helpful

//    @Lazy is used with BCryptPasswordEncoder here because we are creating
//    bean of BCryptPasswordEncoder in this SecurityConfiguration class and
//    for same claas we are giving BCryptPasswordEncoder as a dependency in
//    the constructor. so here it becomes a cyclic dependency and spring will
//    not be able to resolve this on its own. so there can be 2 ways to
//    resolve it. First approach is we mark it @Lazy so that
//    BCryptPasswordEncoder will get lately initialized and before that
//    SecurityConfiguration class will have time to properly initialize.
//    Second approach,we can be to create a separate class and shift the @Bean
//    method to that so that there is no cyclic dependency. Third approach is
//    use field or setter injection

    @Autowired
    public SecurityConfiguration( DataSource dataSource,
                                  @Lazy BCryptPasswordEncoder encoder) {
        this.dataSource = dataSource;
        this.encoder = encoder;
    }

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
