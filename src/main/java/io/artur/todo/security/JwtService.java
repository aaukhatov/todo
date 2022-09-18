package io.artur.todo.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Autowired
    private KeyStore keyStore;

    @Value("${app.security.jwt.keystore-location}")
    private String keyStorePath;

    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;

    @Value("${app.security.jwt.key-alias}")
    private String keyAlias;

    @Value("${app.security.jwt.ttl-minute}")
    private Integer ttl;

    private final Clock systemUTC = Clock.systemUTC();

    public Token ofOAuth2(OAuth2AuthenticationToken oauthToken) {
        try {
            UserPrincipal user = (UserPrincipal) oauthToken.getPrincipal();

            RSAKey rsaKey = RSAKey.load(keyStore, keyAlias, keyStorePassword.toCharArray());

            JWSHeader header = new JWSHeader
                    .Builder(JWSAlgorithm.RS256)
                    .keyID(rsaKey.getKeyID())
                    .build();

            Instant now = Instant.now(systemUTC);
            Date expiresAt = new Date(now.plus(ttl, ChronoUnit.MINUTES).toEpochMilli());

            JWTClaimsSet claimsSet = new JWTClaimsSet
                    .Builder()
                    .jwtID(UUID.randomUUID().toString())
                    .expirationTime(expiresAt)
                    .claim("id", user.getId())
                    .claim("name", user.getName())
                    .claim("email", user.getEmail())
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            JWSSigner signer = new RSASSASigner(rsaKey);
            signedJWT.sign(signer);

            return new Token(signedJWT.serialize());
        } catch (JOSEException | KeyStoreException e) {
            throw new JwtGenerateException("Unable to load JKS keystore", e);
        }
    }
}
