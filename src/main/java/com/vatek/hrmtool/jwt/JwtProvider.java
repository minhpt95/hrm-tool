package com.vatek.hrmtool.jwt;

import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.util.DateUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

@Component
@Log4j2
public class JwtProvider {

    @Value("${hrm.app.jwtSecret}")
    private String jwtSecret;

    @Value("${hrm.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        return generateJwtToken(userPrincipal);
    }

    public String generateJwtToken(UserPrinciple userPrincipal) {
        return generateTokenFromEmail(userPrincipal.getEmail());
    }

    public String generateTokenFromEmail(String email){

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts
                .builder()
                .subject(email)
                .issuedAt(DateUtils.convertInstantToDate(DateUtils.getInstantNow()))
                .expiration(DateUtils.convertInstantToDate(Instant.now().plus(jwtExpiration,ChronoUnit.SECONDS)))
                .signWith(key)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return getSignedClaims(token).getPayload().getSubject();
    }

    public Long getRemainTimeFromJwtToken(String token) {
        return getSignedClaims(token)
                .getPayload()
                .getExpiration()
                .getTime() - DateUtils.getInstantNow().get(ChronoField.MILLI_OF_SECOND);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            return getRemainTimeFromJwtToken(authToken) > 0;
        } catch (Exception e) {
            log.error("Error validateJwtToken -> Message : ",e);
        }
        return false;
    }

    private Jws<Claims> getSignedClaims(String authToken) {

        SecretKey secret = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(authToken);
    }
}
