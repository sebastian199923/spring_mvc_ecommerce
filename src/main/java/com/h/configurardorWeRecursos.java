package com.h;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//Configuracion de los recursos dinamicos como imagenes
@Configuration
public class configurardorWeRecursos implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/imagenes-productos/**")
	            .addResourceLocations(
	                "file:src/main/resources/static/imagenes/",  
	                "file:imagenes-productos/"                             
	            );
	}
}