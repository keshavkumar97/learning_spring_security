package org.example.configuration;

import org.example.dao.User;
import org.example.dao.UserDtl;
import org.example.repositories.SpringSEcurityExampleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    SpringSEcurityExampleRepo repo;

    @Override
    public UserDtl loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from your data source
        // For example, from a database or an in-memory store
        User user = repo.findUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("user found: "+user);
        return new UserDtl(user);
    }


}

