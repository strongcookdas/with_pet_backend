package com.ajou_nice.with_pet.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final CookieUtil cookieUtil;
    @Value("${jwt.token.secret}")
    private String key;
    private final long expireTimeMs = 1000 * 60L;

    private Jws<Claims> extractClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    }

    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = extractClaims(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId(String token) {
        return extractClaims(token).getBody().getSubject();
    }

    public String getUserRole(String token) {
        return extractClaims(token).getBody().get("ROLE").toString();
    }

    public String createToken(String id, String userRole) {
        //user pk 값 insert
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("ROLE", userRole);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(getUserId(token), null,
                List.of(new SimpleGrantedAuthority(getUserRole(token))));
    }

    //쿠키에서 토큰 파싱
    public String getToken(HttpServletRequest request) {
        String token = cookieUtil.getCookieValue(request, "token");
        log.info("token : {}", token);
        return token;
    }
}
