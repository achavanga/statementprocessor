package nl.rabobank.customer.statementprocessor.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * A custom filter that redirects requests to the root URL ("/") to the Swagger UI page
 * ("/swagger-ui.html") in a Spring Boot application.
 * This filter is intended to be used as part of a Spring Boot application where
 * users accessing the root URL are automatically redirected to Swagger UI for API documentation.
 * The filter extends {@link GenericFilterBean} for integration with Spring's filter chain
 * and provides an easy way to implement custom filtering logic for HTTP requests.
 */
@Component
public class RedirectToSwaggerFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check if the request is to the root URL
        if (httpRequest.getRequestURI().equals("/")) {
            httpResponse.sendRedirect("/swagger-ui.html");
            return;
        }
        chain.doFilter(request, response);
    }
}