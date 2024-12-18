package com.h.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

//Controlador encargado de gestionar las excepciones de manera global
@RestControllerAdvice
public class GlobalExceptionHandler {

	String autenticacion;

	public String obtenerUsuarioAutenticado(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {

		if (userDetails == null) {
			autenticacion = "ADMIN";
		} else {
			boolean hasUserRole = userDetails.getAuthorities().stream()
					.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));
			if (hasUserRole) {
				autenticacion = "USER";
			} else {
				autenticacion = "USER";
			}
		}

		return autenticacion;
	}

	// Maneja cualquier excepción de tipo RuntimeException
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleRuntimeException(RuntimeException e, Model model) {

		if (autenticacion.equals("ADMIN")) {
			model.addAttribute("mensaje", "/Admin/home");
			return "usuarios/error.html";
		} else {
			model.addAttribute("mensaje", "http://localhost:8080/");
			return "usuarios/error.html";
		}
	}

	// Maneja excepciones de base de datos
	@ExceptionHandler(DataAccessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleDataAccessException(DataAccessException e, Model model) {
		if (autenticacion.equals("ADMIN")) {
			model.addAttribute("mensaje", "/Admin/home");
			return "usuarios/error.html";
		} else {
			model.addAttribute("mensaje", "http://localhost:8080/");
			return "usuarios/error.html";
		}
	}
}
