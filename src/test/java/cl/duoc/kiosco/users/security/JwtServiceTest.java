package cl.duoc.kiosco.users.security;
import cl.duoc.kiosco.users.model.User;
import cl.duoc.kiosco.users.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JwtServiceTest {
    @Autowired
    private JwtService jwtService;
    private User testUser;
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setActive(true);
        testUser.setRole(UserRole.ADMIN);
    }
    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(testUser);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }
    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(testUser);
        String username = jwtService.extractUsername(token);
        assertEquals("test@example.com", username);
    }
    @Test
    void testIsTokenValid() {
        String token = jwtService.generateToken(testUser);
        assertTrue(jwtService.isTokenValid(token, "test@example.com"));
        assertFalse(jwtService.isTokenValid(token, "wrong@example.com"));
    }
    @Test
    void testGetExpirationMs() {
        long expiration = jwtService.getExpirationMs();
        assertEquals(86400000, expiration);
    }
}
