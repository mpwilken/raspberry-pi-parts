package us.thirdbase.rspi.parts.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@Service
public class SecretService {

    private final JwtTokenConfigurationProperties jwtTokenConfigurationProperties;
    private final Map<String, String> secrets = new HashMap<>();
    private final SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter() {
        @Override
        public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
            return TextCodec.BASE64.decode(secrets.get(header.getAlgorithm()));
        }
    };

    @Autowired
    public SecretService(JwtTokenConfigurationProperties jwtTokenConfigurationProperties) {
        this.jwtTokenConfigurationProperties = jwtTokenConfigurationProperties;
    }

    @PostConstruct
    public void setup() {
        refreshSecrets();
    }

    public SigningKeyResolver getSigningKeyResolver() {
        return signingKeyResolver;
    }

    public byte[] getHS256SecretBytes() {
        return TextCodec.BASE64.decode(secrets.get(SignatureAlgorithm.HS256.getValue()));
    }

    byte[] getHS384SecretBytes() {
        return TextCodec.BASE64.decode(secrets.get(SignatureAlgorithm.HS384.getValue()));
    }

    byte[] getHS512SecretBytes() {
        return TextCodec.BASE64.decode(secrets.get(SignatureAlgorithm.HS512.getValue()));
    }

    private void refreshSecrets() {
        secrets.put(SignatureAlgorithm.HS256.getValue(), TextCodec.BASE64.encode(jwtTokenConfigurationProperties.getHs256()));
        secrets.put(SignatureAlgorithm.HS384.getValue(), TextCodec.BASE64.encode(jwtTokenConfigurationProperties.getHs384()));
        secrets.put(SignatureAlgorithm.HS512.getValue(), TextCodec.BASE64.encode(jwtTokenConfigurationProperties.getHs512()));
    }
}
