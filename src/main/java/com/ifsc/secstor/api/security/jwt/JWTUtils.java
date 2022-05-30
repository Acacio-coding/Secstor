package com.ifsc.secstor.api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ifsc.secstor.api.config.SecstorConfig;
import com.ifsc.secstor.api.model.UserModel;
import com.ifsc.secstor.api.util.BeanUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.ifsc.secstor.api.util.Constants.*;

public class JWTUtils {
    private final Algorithm algorithm;

    public JWTUtils() {
        SecstorConfig config = BeanUtil.getBean(SecstorConfig.class);
        this.algorithm = Algorithm.HMAC256(config.authSecret().getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, String> createTokens(User user, HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.DATE, 1);

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(ROLE, user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(this.algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(calendar.getTime())
                .withIssuer(request.getRequestURL().toString())
                .sign(this.algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, accessToken);
        tokens.put(REFRESH_TOKEN, refreshToken);

        return tokens;
    }

    public Map<String, String> createTokenByRefreshToken(String refreshToken, UserModel user, HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.MONTH, 1);

        Collection<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority(user.getRole().name()));

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(ROLE, role
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(this.algorithm);

        Map<String, String> token = new HashMap<>();
        token.put(ACCESS_TOKEN, accessToken);
        token.put(REFRESH_TOKEN, refreshToken);
        return token;
    }

    public String getTokenUsername(String token) {
        JWTVerifier verifier = JWT.require(this.algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getSubject();
    }

    public UsernamePasswordAuthenticationToken verifyToken(String token) {
        JWTVerifier verifier = JWT.require(this.algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim(ROLE).asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roles[0]));

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
