package com.examtestfinancialapp.examtestfinancialapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Au lieu de "*", spécifiez les origines autorisées
        // Pour un environnement de production, vous devriez limiter ceci
        config.setAllowedOriginPatterns(Arrays.asList("*"));

        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Attention: setAllowCredentials(true) ne fonctionne pas avec
        // allowedOrigin("*")
        // Pour un vrai environnement public, définissez cette valeur à false ou
        // spécifiez des origines précises
        config.setAllowCredentials(false);

        source.registerCorsConfiguration("/api/scores/**", config);
        return new CorsFilter(source);
    }
}