package com.example.money.global.jwt;

import com.example.money.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
public class TokenProvider {

    public static final String AUTHORITIES_KEY = "auth";
    // access token 유효시간 (millisecond)
    private final long ACCESS_TOKEN_DURATION_MS;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration.access}") long accessTokenDuration) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.ACCESS_TOKEN_DURATION_MS = accessTokenDuration * 1000;
    }

    /**
     * 토큰 생성 : 유저와 생성할 토큰 타입을 변수로 받음.
     * @param member Member 객체
     */
    public String generateToken(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_DURATION_MS);

        return createToken(member, expiry);
    }

    private String createToken(Member user, Date expiry) {
        return Jwts.builder()
                // token subject를 사용자 아이디로 저장
                .setSubject(user.getUsername())
                .setExpiration(expiry)
                .setIssuedAt(new Date())
                // claim에 권한, id 값 저장
                .addClaims(Map.of(
                        AUTHORITIES_KEY, "",
                        "userId", user.getId()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 유효한 토큰인지 확인하여 boolean 값을 반환합니다.
     * */
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Set<SimpleGrantedAuthority> authority = Collections.singleton(
                new SimpleGrantedAuthority(claims.get(AUTHORITIES_KEY).toString())
        );

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authority),
                token,
                authority
        );
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Long getUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
