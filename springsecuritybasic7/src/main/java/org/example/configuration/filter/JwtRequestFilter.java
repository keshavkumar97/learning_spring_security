package org.example.configuration.filter;

import org.example.configuration.CustomUserDetailsService;
import org.example.configuration.util.JwtUtil;
import org.example.dao.UserDtl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    private final CustomUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        System.out.println("filtering request");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDtl userDtl =
                    this.userDetailsService.loadUserByUsername(username);
            System.out.println(userDtl);
            if (jwtUtil.validateToken(jwt, userDtl)) {
                //if token is valid then create a token of details and set it to the
                // securitycontextholder
                System.out.println("jwt authentication");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDtl, null,
                                userDtl.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));    //set additional details like remote address and session id
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); // user authenticated so set details in securitycontextholder for further use

                System.out.println("User authenticated: " + usernamePasswordAuthenticationToken.getName());
                System.out.println("Authorities: " + usernamePasswordAuthenticationToken.getAuthorities());
            }
        }
        chain.doFilter(request, response);  //resume filter
    }
}
