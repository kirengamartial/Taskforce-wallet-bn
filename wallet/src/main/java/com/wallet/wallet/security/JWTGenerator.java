package com.wallet.wallet.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTGenerator {

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date CurrentDate = new Date();
        Date expireDate = new Date(CurrentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
        return token;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT has expired");
        } catch (MalformedJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT token");
        } catch (SignatureException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT signature does not match");
        } catch (Exception ex) {
            System.out.println("Authorization Header: " + token);
            throw new AuthenticationCredentialsNotFoundException("JWT is invalid");
        }
    }
}