package com.depromeet.booboo.config;

import com.depromeet.booboo.application.security.JwtFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:jwt.yml")
public class JwtConfig {
    @Value("${jwt.tokenIssuer:defaultTokenIssuer}")
    private String tokenIssuer;
    @Value("${jwt.tokenSigningKey:defaultTokenSigningKey}")
    private String tokenSigningKey;

    @Bean
    public JwtFactory jwtFactory() {
        return new JwtFactory(tokenIssuer, tokenSigningKey);
    }
}