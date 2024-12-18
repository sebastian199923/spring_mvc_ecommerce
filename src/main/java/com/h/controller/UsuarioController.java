package com.h.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.h.entity.Usuario;
import com.h.entity.detalleOrden;
import com.h.entity.Orden;
import com.h.service.IDetalleOrdenService;
import com.h.service.IOrdenService;
import com.h.service.IUsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/User")
public class UsuarioController {

	/// Encripta las contraseña
	BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();
	// Usar metodos crud de la clase usuario
	@Autowired
	private IUsuarioService userservice;
	// Usar metodos de orden
	@Autowired
	private IOrdenService ordenservice;
	// Usar metodos de orden
	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	@GetMapping("/ListarUsuario")
	public String listarUsuario(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
			Model model) {
		
		// Se obtiene el usuario que esta loggueado
		String correo = userDetails.getUsername();
		Optional<Usuario> optionalUsuario = userservice.findByCorreo(correo);
		if (optionalUsuario.isPresent()) {
			model.addAttribute("usuario", optionalUsuario.get());
		} else {
			return "Admin/home";
		}
		return "usuarios/infousuario";
	}

	@PostMapping("/Editar")
	@ResponseBody
	public ResponseEntity<String> editarUsuario(@ModelAttribute Usuario usuario) {
		boolean usuarioActualizado = userservice.editarUsuario(usuario);

		if (usuarioActualizado) {
			return ResponseEntity.ok("Usuario actualizado correctamente.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		}
	}

	// Direccionar a la interfaz de registro de usuario
	@GetMapping("/Public/Registro")
	public String crearUsuario() {
		return "usuarios/registro";
	}

	@PostMapping("/EditarContrasena")
	@ResponseBody
	public Map<String, String> editarContrasena(@RequestParam("id") Integer id,
			@RequestParam("contrasena") String contrasena) {

		Map<String, String> response = new HashMap<>();
		Optional<Usuario> usuarioOptional = userservice.findById(id);

		// Valida la existencia del usuario
		if (!usuarioOptional.isPresent()) {
			response.put("status", "error");
			response.put("message", "Usuario no encontrado");
			return response; // Usuario no encontrado, devuelve la respuesta
		}

		// Validar la contraseña
		Usuario usuario = usuarioOptional.get();
		if (userservice.verificarContrasena(contrasena, usuario)) {
			response.put("status", "success");
			response.put("redirectUrl", "/RecuperarContrasena/EstablecerContrasena");
		} else {
			response.put("status", "error");
			response.put("message", "La contraseña no coincide");
		}

		return response;
	}

	@PostMapping("/Public/Guardar")
	public String guardar(Usuario user, RedirectAttributes redirectAttributes) {

		String resultado = userservice.guardarUsuario(user);

		if (resultado.equals("El usuario ha sido registrado")) {
			redirectAttributes.addFlashAttribute("mensajeAlerta", "El usuario ha sido registrado.");
			redirectAttributes.addFlashAttribute("tipoAlerta", "success");
			return "redirect:/User/Login";
		} else if (resultado.equals("cedulaUnica")) {
			redirectAttributes.addFlashAttribute("mensajeAlerta", "La cedula ya ha sido registrada en el sistema.");
			redirectAttributes.addFlashAttribute("tipoAlerta", "error");
			return "redirect:/User/Public/Registro";
		} else if (resultado.equals("correoUnico")) {
			redirectAttributes.addFlashAttribute("mensajeAlerta", "El correo ya ha sido registrado en el sistema.");
			redirectAttributes.addFlashAttribute("tipoAlerta", "error");
			return "redirect:/User/Public/Registro";
		} else {
			redirectAttributes.addFlashAttribute("mensajeAlerta", "Ocurrió un error inesperado.");
			redirectAttributes.addFlashAttribute("tipoAlerta", "error");
			return "redirect:/User/Public/Registro";
		}
	}

	// Metodo para entrar luego del login
	@GetMapping("/Entrar")
	public String entrar(Usuario user, HttpSession sesion,
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
			HttpServletRequest request) {

		Optional<Usuario> usuario = userservice.findByCorreo(userDetails.getUsername());
		if (usuario.isPresent()) {
            
		    //Para establecer el ultimo inicio de sesion
			Usuario usuarioLogeado = usuario.get();
			usuarioLogeado.setUltimoLogin(LocalDateTime.now());
			userservice.editarUsuario(usuarioLogeado);

			sesion.setAttribute("usuarioSesion", usuario.get().getId());
			String vistaMostrar = (String) request.getSession().getAttribute("vistaMostrar");

			// Valida que tipo de usuario se logueo
			if (usuario.get().getTipo().equals("USER")) {

				if (vistaMostrar.equals("vistaMostrarCarro")) {
					return "redirect:/Public/MostrarCarro";
				} else {
					return "redirect:/";
				}

			} else {
				return "redirect:/Admin/home";
			}
		} else {
			return "usuarios/login";
		}
	}

	// Cuando falla el login
	@GetMapping("/Login")
	public String login(HttpSession session, Model model) {
		String mensajeAlerta = (String) session.getAttribute("mensajeAlerta");
		if (mensajeAlerta != null) {
			model.addAttribute("mensajeAlerta", mensajeAlerta);
			model.addAttribute("tipoAlerta", "error");
			session.removeAttribute("mensajeAlerta");
		}
		return "usuarios/login";
	}

	/// Listar todas las ordenes o compras que ha hecho un determinado usuario
	@GetMapping("/ProductosComprados")
	public String productosComprados(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails, Model modelo) {
		/// Se busca el usuario que esta actuamente logueado
		Optional<Usuario> user = userservice.findByCorreo(userDetails.getUsername());
		if (user.isPresent()) {
			// Se buscan las ordenes que estan asociadas a ese usuario
			List<Orden> ordenesUsuario = ordenservice.findByUsuario(user.get());

			modelo.addAttribute("compras", ordenesUsuario);
			return "usuarios/productoscomprados";
		}
		return "usuarios/home";

	}

	// Detalle de compra por cada usuario
	@GetMapping("/DetalleCompra/{id}")
	public String compraDetalle(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
			Model modelo, @PathVariable Integer id) {
		// Se tre el detalle orden de determinada orden
		List<detalleOrden> detalleOrden = detalleOrdenService.findByOrdenId(id);
		// Se obtiene el tipo de usuario que esta en la sesion
		Optional<Usuario> usuario = userservice.findByCorreo(userDetails.getUsername());

		if (usuario.isPresent()) {
			modelo.addAttribute("compradetalles", detalleOrden);
			modelo.addAttribute("tipoUsuario", usuario.get().getTipo());
			return "usuarios/compradetalle";
		}
		return "usuarios/home";

	}

	// Metodo para cerrar la sesion del usuario
	@GetMapping("/CerrarSesion")
	public String cerrarSesion() {

		return "redirect:/HomeUser/Listar";
	}
}
