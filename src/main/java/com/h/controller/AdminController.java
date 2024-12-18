package com.h.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h.entity.Producto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.h.service.IProductoService;
import com.h.service.IUsuarioService;
import jakarta.persistence.Tuple;

@Controller
@RequestMapping("/Admin")
public class AdminController {

	/// Usar metodos crud de productos
	@Autowired
	private IProductoService productoService;

	/// Usar metodos crud para los usuarios
	@Autowired
	private IUsuarioService usuarioService;

	// Metodo encargado de listar los productos en el home del Administrador
	@GetMapping("/home")
	public String home(Model model) {
		List<Producto> produ = productoService.findAll();
		model.addAttribute("produ", produ);
		return "Admin/home";
	}

    //Lista todos los usuarios con su cantidad de ventas y el total de dinero
	@GetMapping("/GestionUsuarios")
	public String obtenerUsuariosConCompras(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int tamaño) { 
        Pageable paginado = PageRequest.of(page, tamaño);
		Page<Tuple> usuarios = usuarioService.obtenerUsuariosConCompras(paginado);
        model.addAttribute("usuarios", usuarios);
		model.addAttribute("totalPages", usuarios.getTotalPages()); 
		model.addAttribute("currentPage", usuarios.getNumber());
		model.addAttribute("totalItems", usuarios.getTotalElements());
        return "Admin/gestionusuarios";
	}
	
	@PostMapping("/BuscarUsuarioAjax")
	@ResponseBody
	public Map<String, Object> buscarUsuarioAjax(@RequestParam String cedulaUsuario) {
		PageRequest paginado = PageRequest.of(0, 10);
		Page<Tuple> usuarios;
		if(cedulaUsuario.equals("")) {
			usuarios = usuarioService.obtenerUsuariosConCompras(paginado);
		}else {
			usuarios = usuarioService.usuarioCedulaComprasCedula(paginado, cedulaUsuario);
		}
		//convierte el listado de usuarios en un formato manejable por javascript
	    List<List<Object>> listaUsuarios = usuarios.getContent().stream()
	        .map(tuple -> List.of(
	            tuple.get(0),
	            tuple.get(1), // nombre
	            tuple.get(2), // apellido
	            tuple.get(3), // cédula
	            tuple.get(4), // correo
	            tuple.get(5), // teléfono
	            tuple.get(6), // tipo
	            tuple.get(7), // número de compras
	            tuple.get(8)  // cantidad de dinero
	        ))
	        .toList();
        Map<String, Object> response = new HashMap<>();
	    response.put("usuarios", listaUsuarios);
	    response.put("totalPages", usuarios.getTotalPages());
	    response.put("currentPage", usuarios.getNumber());
	    response.put("totalItems", usuarios.getTotalElements());
        return response;
	}
	

}
