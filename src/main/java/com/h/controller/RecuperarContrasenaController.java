package com.h.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.h.service.IEmailService;
import com.h.service.IUsuarioService;


@Controller
@RequestMapping("/RecuperarContrasena")
public class RecuperarContrasenaController {

	/// Encriptar contraseña
	BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();

	@Autowired
	IEmailService emailService;

	@Autowired
	private IUsuarioService userservice;

	String numeroRandom;
	String correoElectronico;

	// Formulario de enviar correo
	@GetMapping("/EnviarCorreo")
	private String forgotPassword(Model model) {

		return "usuarios/enviarcorreo";
	}

	// Formulario de comparar codigo
	@GetMapping("/Codigo")
	private String compararCodigo(Model model) {
		return "usuarios/compararcodigo";
	}

	// Formulario de establecer nueva contraseña
	@GetMapping("/EstablecerContrasena")
	private String establecerContrasena() {
		return "usuarios/EstablecerContrasena";
	}

	@PostMapping("/EnviarCodigo")
	public String enviarCorreo(@RequestParam("correo") String correo, RedirectAttributes redirectAttributes) {
		try {
			String redireccion = userservice.enviarCodigoRecuperacion(correo);
			redirectAttributes.addFlashAttribute("mensajeAlerta", "Revisa tu correo electronico");
			redirectAttributes.addFlashAttribute("tipoAlerta", "success");
			return redireccion;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensajeAlerta", "Correo no encontrado");
			redirectAttributes.addFlashAttribute("tipoAlerta", "error");
			return "redirect:/RecuperarContrasena/EnviarCorreo";
		}
	}

	@PostMapping("/CompararCodigo")
	private String compararCodigo(@RequestParam("codigo") String codigo, RedirectAttributes redirectAttributes) {

		String respuesta = userservice.compararCodigo(codigo);
		if (respuesta.equals("Ok")) {
			return "redirect:/RecuperarContrasena/EstablecerContrasena";
		} else {
			// En caso de que el codigo no coincida
			redirectAttributes.addFlashAttribute("mensajeAlerta", "Codigo erroneo");
			redirectAttributes.addFlashAttribute("tipoAlerta", "error");
			return "redirect:/RecuperarContrasena/Codigo";
		}
		}

	@PostMapping("/GuardarContrasena")
	private String guardarContrasena(@RequestParam("contrasena") String contrasena,
			RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        boolean vistaEditar = false;
		String respuesta = userservice.guardarContrasena(contrasena, vistaEditar, userDetails);
		return respuesta;

	}

}
