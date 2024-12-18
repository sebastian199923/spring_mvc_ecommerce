package com.h.service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.h.entity.Usuario;
import jakarta.persistence.Tuple;

public interface IUsuarioService {
	//Encontrar usuario en particular
	Optional<Usuario> findById(Integer id);
	//Guardar un usuario nuevo en la bd
	Usuario save (Usuario user);
	
	///Metodo para buscar a un usuario mediante el correo
	Optional <Usuario> findByCorreo(String correo);
	
	//Listar todos los usuarios paginados y con columnas externas a su tabla
	Page<Tuple> obtenerUsuariosConCompras(Pageable pageable);
	
	//Listar todos los usuarios paginados y con columnas externas a su tabla
	Page<Tuple> usuarioCedulaComprasCedula(Pageable pageable,String cedulaUsuario);
	
	//Maneja la logica de editar usuario
	public boolean editarUsuario(Usuario usuario);
	
	//Valida la compracion de la contraseña para el cambio de contraseña
	public boolean verificarContrasena( String contrasena,Usuario usuario);
	
	//Logica para registrar un nuevo usuario
	public String guardarUsuario(Usuario user) ;
	
	//Nuscar un usuario para enviarle correo de recuperacion de contraseña
	public String enviarCodigoRecuperacion(String correo);
	
	//Compara el codigo enviado por correo y el codigo introducido por el usuario para cambiar contraseña
	public String compararCodigo(String codigo);
	
	//Guardar la contraseña que cambia el usuario
	public String guardarContrasena(String contrasena ,boolean vistaEditar,@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails);
	
	
	
	
	

	

	
	

}
