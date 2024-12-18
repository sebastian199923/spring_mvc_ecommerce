package com.h.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//Controller encargado de manejar error de tipo HTTP
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

	@RequestMapping("/error")
	public String handleError(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
			Model model) {

		if (userDetails == null) {
			// Sin loguearse
			model.addAttribute("mensaje", "http://localhost:8080/");
			return "usuarios/error.html";
		} else {
			boolean hasUserRole = userDetails.getAuthorities().stream()
					.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));
			if (hasUserRole) {
				// Rol Usuario
				model.addAttribute("mensaje", "http://localhost:8080/");
				return "usuarios/error.html";
			} else {
				// Rol admin
				model.addAttribute("mensaje", "/Admin/home");
				return "usuarios/error.html";
			}
		}

	}
}