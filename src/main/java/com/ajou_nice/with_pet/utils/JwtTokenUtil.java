package com.ajou_nice.with_pet.utils;

import com.ajou_nice.with_pet.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenUtil {

    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public static String isValid(String token, String key) {
        try {
            extractClaims(token, key);
            return "OK";
        } catch (ExpiredJwtException e) {
            return ErrorCode.EXPIRE_TOKEN.name();
        } catch (Exception e) {
            return ErrorCode.INVALID_TOKEN.name();
        }
    }

    public static Long getUserId(String token, String key) {
        return Long.valueOf(extractClaims(token, key).get("userId").toString());
    }

    public static String createToken(Long userId, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
