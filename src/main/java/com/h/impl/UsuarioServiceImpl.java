package com.h.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.h.Dao.IUsuarioDao;

import com.h.entity.Usuario;
import com.h.service.IEmailService;
import com.h.service.IUsuarioService;

import jakarta.persistence.Tuple;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	/// Encripta las contraseña
	BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();

	// Inyeccion de los metodos crud de jpa
	@Autowired
	private IUsuarioDao usuarioDao;

	String numeroRandom;

	String correoElectronico;

	// Inyeccion de los metodos crud de jpa
	@Autowired
	private IEmailService emailDao;

	// Metodo para listar a un usuario en particular
	@Override
	public Optional<Usuario> findById(Integer id) {

		return usuarioDao.findById(id);
	}

	// Guardar un usuario nuevo en la bd
	@Override
	public Usuario save(Usuario user) {

		return usuarioDao.save(user);
	}

	/// Buscar correo
	@Override
	public Optional<Usuario> findByCorreo(String correo) {

		return usuarioDao.findByCorreo(correo);
	}

	/// Traer usuarios con sus compras y paginado
	@Override
	public Page<Tuple> obtenerUsuariosConCompras(Pageable pageable) {
		return usuarioDao.findUsuario(pageable);
	}

	@Override
	public Page<Tuple> usuarioCedulaComprasCedula(Pageable pageable, String cedulaUsuario) {

		return usuarioDao.findUsuarioCedula(cedulaUsuario, pageable);
	}

	// Logica de editar usuario
	public boolean editarUsuario(Usuario usuario) {
		Optional<Usuario> usuarioOptional = findById(usuario.getId());

		if (usuarioOptional.isPresent()) {
			Usuario usu = usuarioOptional.get();
			usuario.setContrasena(usu.getContrasena());// mantiene la contraseña preexistente
			save(usuario);
			return true;
		} else {
			return false;
		}
	}

	// valida la contraseña ingresada antes de cambiar
	public boolean verificarContrasena(String contrasena, Usuario usuario) {

		// Compara las contraseñas ingresadas
		if (passEncode.matches(contrasena, usuario.getContrasena())) {
			return true;
		}

		return false;
	}

	// Logica para guardar un usuario nuevo
	public String guardarUsuario(Usuario user) {
		// Establecer tipo y codificar la contraseña
		user.setTipo("USER");
		user.setContrasena(passEncode.encode(user.getContrasena()));

		try {
			// Guardar el usuario
			save(user);
			return "El usuario ha sido registrado";
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("cedulaUnica")) {
				return "cedulaUnica";
			} else if (e.getMessage().contains("correoUnico")) {
				return "correoUnico";
			}
			throw e;
		} catch (Exception e) {
			// Manejo de errores generales
			return "Ocurrió un error inesperado.";
		}
	}

	public String enviarCodigoRecuperacion(String correo) {
		Optional<Usuario> user = usuarioDao.findByCorreo(correo);
		if (user.isPresent()) {
			Random random = new Random();
			int numeroAleatorio = random.nextInt(90000) + 10000;
			numeroRandom = Integer.toString(numeroAleatorio);
			emailDao.sendEmailfForgottenPassword(correo, numeroRandom, user.get().getNombre());
			// Guardar o actualizar cualquier información necesaria aquí, si aplica.
			correoElectronico = correo;
			return "redirect:/RecuperarContrasena/Codigo";
		} else {
			return ("El usuario con correo " + correo + " no fue encontrado");
		}
	}

	public String compararCodigo(String codigo) {

		if (codigo.equals(numeroRandom)) {
			return "Ok";
		} else {
			return "No Iguales";
		}
	}

	public String guardarContrasena(String contrasena, boolean vistaEditar,
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {

		// En caso de que el usuario esté logueado se obtiene el correo de Spring
		// Security
		if (userDetails != null) {
			correoElectronico = userDetails.getUsername();
			vistaEditar = true;
		}

		// Buscar al usuario por correo
		Optional<Usuario> usuarioOptional = findByCorreo(correoElectronico);
		if (usuarioOptional.isPresent()) {
			// Se obtiene el usuario que se buscó por correo
			Usuario usu = usuarioOptional.get();
			usu.setContrasena(contrasena);
			usu.setContrasena(passEncode.encode(usu.getContrasena()));
			save(usu);

			if (vistaEditar) {
				return "redirect:/User/ListarUsuario";
			} else {
				return "redirect:/User/Login";
			}
		}
		return "redirect:/User/Login";

	}

}
