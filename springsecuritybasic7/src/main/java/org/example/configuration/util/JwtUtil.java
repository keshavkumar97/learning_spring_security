package org.example.configuration.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.dao.AuthRequest;
import org.example.dao.UserDtl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
public class JwtUtil {

    private final String secret = "your_secret_key";

    public String extractUsername(String token) {
        String name = JWT.decode(token).getSubject();
        System.out.println(name);
        return name;
    }

    //
//    public Date extractExpiration(String token) {
//        return ;
//    }
//
    private void extractAllClaims(String token) {
        System.out.println(JWT.decode(token).getPayload());
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(Date.from(Instant.now()));
//    }

    public String generateToken(String username) {
        return createToken(username);
    }

    private String createToken(String username) {
        return JWT.create().withSubject(username)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusMillis(2000))
//                .withClaim("role", userDtl.getRole())
                .sign(Algorithm.HMAC256(secret));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
//        extractAllClaims(token);
        return (username.equals(userDetails.getUsername()));
    }
}

