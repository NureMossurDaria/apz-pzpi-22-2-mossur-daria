package ua.nure.mossurd.blooddosyst.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${blooddo.auth.secret-key}")
    private String secretKey;

    @Value(("${auth.token-expiration-ms:3600000}")) // Default: 1 hour
    private Integer tokenExpiration;

    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claims()
                .add("authorities",
                        userPrincipal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList())
                .and()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
