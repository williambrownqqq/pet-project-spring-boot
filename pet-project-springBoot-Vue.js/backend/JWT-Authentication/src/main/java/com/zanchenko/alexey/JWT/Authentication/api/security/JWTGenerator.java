package com.zanchenko.alexey.JWT.Authentication.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.zanchenko.alexey.JWT.Authentication.api.security.SecurityConstants.JWT_EXPIRATION;
import static com.zanchenko.alexey.JWT.Authentication.api.security.SecurityConstants.JWT_SECRET;

@Component
public class JWTGenerator { // class which is going to generate JWT token

    public String generateToken(Authentication authentication){
        String username = authentication.getName(); // get the username from the actual authentication object
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

        String token =  Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // alghorithm that does the signing
                .compact();
        return token;
    }

    public String getUsernameFromJWT(String token){ // we need to be able to get the username from the JWT and we also need to be able to validate
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJwt(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }
}
