package com.ia.para.devs.skybook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig — configuração global de CORS da aplicação SkyBook.
 *
 * <p>Permite que o frontend React (porta 5173) consuma a API REST
 * sem bloqueio de política de mesma origem (Same-Origin Policy).</p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registra as regras de CORS para todos os endpoints da API.
     *
     * @param registry registro de mapeamentos CORS do Spring MVC.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
