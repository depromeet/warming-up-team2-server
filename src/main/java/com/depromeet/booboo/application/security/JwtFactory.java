package com.depromeet.booboo.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.depromeet.booboo.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class JwtFactory {
    private static final Pattern PATTERN_AUTHORIZATION_HEADER = Pattern.compile("[Bb]earer (.+)");
    private static final String CLAIM_NAME_MEMBER_ID = "id";

    private final String tokenIssuer;
    private final String tokenSigningKey;
    private Algorithm algorithm;
    private JWTVerifier jwtVerifier;

    public JwtFactory(String tokenIssuer, String tokenSigningKey) {
        this.tokenIssuer = tokenIssuer;
        this.tokenSigningKey = tokenSigningKey;
    }

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(tokenSigningKey);
        jwtVerifier = JWT.require(algorithm).build();
    }

    public String generateToken(Member member) {
        return JWT.create()
                .withIssuer(tokenIssuer)
                .withClaim(CLAIM_NAME_MEMBER_ID, member.getMemberId())
                .sign(algorithm);
    }

    public Optional<Long> decodeToken(String authorizationHeader) {
        try {
            final String token = extractAccessToken(authorizationHeader);
            final DecodedJWT decodedJWT = jwtVerifier.verify(token);
            final Map<String, Claim> claims = decodedJWT.getClaims();
            final Claim idClaim = claims.get(CLAIM_NAME_MEMBER_ID);
            if (idClaim == null) {
                log.warn("Failed to decode jwt token. header:" + authorizationHeader);
                return Optional.empty();
            }
            return Optional.of(idClaim.asLong());
        } catch (IllegalArgumentException ex) {
            log.warn("Failed to extract token from header. header:" + authorizationHeader, ex);
            return Optional.empty();
        } catch (JWTVerificationException ex) {
            log.warn("Invalid access Token. header:" + authorizationHeader, ex);
            return Optional.empty();
        }
    }

    private String extractAccessToken(String header) {
        if (StringUtils.isEmpty(header)) {
            throw new IllegalArgumentException("Invalid authorization header. header:" + header);
        }
        final Matcher matcher = PATTERN_AUTHORIZATION_HEADER.matcher(header);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid authorization header. header:" + header);
        }
        return matcher.group(1);
    }
}

