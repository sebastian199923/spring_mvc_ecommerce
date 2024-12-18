package com.h.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.h.Dao.IUsuarioDao;
import com.h.entity.Usuario;
import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final IUsuarioDao userRepository;
   
	public UserDetailsServiceImpl(IUsuarioDao userRepository) {
		this.userRepository = userRepository;
		
	}
    
    //Se guardan los datos del usuario en una sesion
    @Autowired
    private HttpSession sesion;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Busca al usuario en la base de datos usando el correo electrónico
		Usuario usuario = userRepository.findByCorreo(email).orElseThrow(
				() -> new UsernameNotFoundException("Usuario no encontrado con el correo electrónico: " + email));
		sesion.setAttribute("usuarioSesion", usuario);
	    // Convierte Usuario en un objeto UserDetails y devuelve para que spring security pueda hacer las validaciones
		return User.builder().username(usuario.getCorreo()).password(usuario.getContrasena()).roles(usuario.getTipo())
				.build();
	}
}
