package cl.duoc.kiosco.users.security;
import cl.duoc.kiosco.users.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio responsable de generar y validar JSON Web Tokens (JWT).
 *
 * Un JWT tiene 3 partes separadas por puntos: header.payload.firma.
 * Aquí se firma con HMAC-SHA usando una clave secreta (app.jwt.secret); cualquiera
 * puede leer el payload, pero solo quien tiene la clave puede generar una firma
 * válida, por eso no debe filtrarse el secreto.
 */
@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.expiration-ms:86400000}")
    private long expirationMs;
    private SecretKey signingKey;
    @PostConstruct
    void init() {
        signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(User user) {
        return Jwts.builder()
                .claims(Map.of("role", user.getRole().name(), "active", user.isActive()))
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey)
                .compact();
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public boolean isTokenValid(String token, String expectedUsername) {
        return expectedUsername.equals(extractUsername(token)) && !extractClaim(token, Claims::getExpiration).before(new Date());
    }
    public long getExpirationMs() {
        return expirationMs;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
