package com.heuy.Security_test.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//validating jwt
@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "POMbmtkcIH7aFUXN2JpIOtWmzAPayqGiB4SyF2wbQhhMwIekEqgkltsetbXLwPwRWPlqFbjcPhMW97VXNcj3roQw46X64fmGos95dkwyOijobhjsar8OXZj9sl3GfU0Fdz5mXpAMq4Oig78VGAXAEjvX0wxHNaocwN0XROeMxEbfbIvwi5QwZUaGnPmqYXLtWrgrqFGAZW98nSiIbYoade9tFnzvOACZeMSQD58uxqKbvKam6TclhSsJYWTt2J35";
    public String extractUserName(String token) {
        //the subject is the email or the username... the original identity of the principal sha
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    //creating a method to generate the token.. now what is the token?
    public String generateToken(Map<String, Object> extraClaims, UserDetails userdetails){
            return Jwts
                    .builder()
                    .claims(extraClaims)
                    .subject(userdetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis()+1000*60*24))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            //.compact generates and returns the token
        }

    //method to validate a token
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }


    //what does the extraclaim does?
        //what are claims? claims are the important features of spring security e.g: username, passwords, expriedat, createdat.. etc.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
