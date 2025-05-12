package com.examtestfinancialapp.examtestfinancialapp.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ContentTypeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Optionnel : ajouter du logging si nécessaire
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Forcer le Content-Type à application/json si non défini
        if (httpRequest.getContentType() == null || httpRequest.getContentType().isEmpty()) {
            httpRequest = new ContentTypeRequestWrapper(httpRequest);
        }

        chain.doFilter(httpRequest, response);
    }

    @Override
    public void destroy() {
        // Méthode de nettoyage optionnelle
    }

    private static class ContentTypeRequestWrapper extends HttpServletRequestWrapper {
        public ContentTypeRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getContentType() {
            return "application/json";
        }
    }
}