package cl.duoc.kiosco.users.security.handler;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de los handlers que escriben la respuesta de error en JSON
 * para 401 ({@link RestAuthenticationEntryPoint}) y 403 ({@link RestAccessDeniedHandler}).
 */
class SecurityHandlersTest {

    @Test
    void entryPoint_escribe401ConJson() throws IOException, ServletException {
        RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response, new BadCredentialsException("bad"));

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertTrue(response.getContentAsString().contains("No autenticado"));
    }

    @Test
    void accessDeniedHandler_escribe403ConJson() throws IOException, ServletException {
        RestAccessDeniedHandler deniedHandler = new RestAccessDeniedHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        deniedHandler.handle(request, response, new AccessDeniedException("denied"));

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertTrue(response.getContentAsString().contains("permisos"));
    }
}
