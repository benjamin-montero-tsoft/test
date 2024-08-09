package ar.com.latam.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

	private String accessDeniedUrl;

    public void handle(HttpServletRequest request, HttpServletResponse response, 
    		AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendRedirect(accessDeniedUrl);
    }

    public String getAccessDeniedUrl() {
        return accessDeniedUrl;
    }

    public void setAccessDeniedUrl(String accessDeniedUrl) {
        this.accessDeniedUrl = accessDeniedUrl;
    }
}
