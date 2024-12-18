package com.h.impl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	private final UserDetailsServiceImpl userDetailsService; // Inyección de la clase UserDetailsServiceImpl
    public SpringSecurityConfig(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService; // Asignación del UserDetailsServiceImpl

	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) 
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/").permitAll() //Home general sin necesidad de loguearse
						.requestMatchers("/Public/**").permitAll() //Metodos del home general sin necesidad de loguearse
						.requestMatchers("/User/Public/**").permitAll() //Para que la funcion de registrarse este disponible para todos sin loguearse
						.requestMatchers("/RecuperarContrasena/**").permitAll() //recuperar contraseña sea disponible para todos sin loguearse
						.requestMatchers("/Admin/**").hasRole("ADMIN")
						.requestMatchers("/Categorias/**").hasRole("ADMIN")
						.requestMatchers("/Orden/**").hasRole("ADMIN")
						.requestMatchers("/Productos/**").hasRole("ADMIN")
						.requestMatchers("/imagenes/**").permitAll()
						.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
						.requestMatchers("/imagenes-productos/**").permitAll()
                        .anyRequest().authenticated() 
				).formLogin(form -> form 
						.loginPage("/User/Login") // Página personalizada de login
						.usernameParameter("correo") 
						.passwordParameter("contrasena") 
						.loginProcessingUrl("/login").permitAll().defaultSuccessUrl("/User/Entrar", true)//A donde se dirige despues de hacer las validaciones
						
						//Cuando el login falla
						.failureHandler((request, response, exception) -> {
							request.getSession().setAttribute("mensajeAlerta", "Credenciales invalidas");
							String vistaMostrar = request.getParameter("vistaMostrar");
							HttpSession sesion = request.getSession();
						    sesion.removeAttribute("usuarioSesion");
						    
                            if(vistaMostrar.equals("vistaMostrarCarro")) {
								response.sendRedirect("/Public/MostrarCarro");
							}else {
								response.sendRedirect("/User/Login");	
							}
                            
						//Cuando el login es exitoso
						}).successHandler((request, response, authentication) -> {
							String vistaMostrar = request.getParameter("vistaMostrar");
							request.getSession().setAttribute("vistaMostrar", vistaMostrar);
							response.sendRedirect("/User/Entrar");
						}))
				//Cuando se desloguea
				.logout(logout -> logout 
						.logoutUrl("/logout") 
						.logoutSuccessUrl("/User/Login").invalidateHttpSession(true) 
						.clearAuthentication(true)
						.permitAll() 
				).userDetailsService(userDetailsService);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}
}
