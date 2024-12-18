package com.h.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h.entity.Producto;
import com.h.entity.Usuario;
import com.h.exception.ProductoNoEncontradoException;
import com.h.service.IUsuarioService;
import com.h.service.ICarritoService;
import com.h.service.ICategoriaService;
import com.h.service.IOrdenService;
import com.h.service.IProductoService;

@Controller
public class HomeController {

	// Inyeccion para hjacer uso de los metodos crud de la orden
	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IProductoService productoService;

	@Autowired
	private ICategoriaService categoriaService;

	@Autowired
	private ICarritoService carritoService;

	// Contador en el carrito
	@ModelAttribute("contadorCarrito")
	public int contadorCarrito() {
		return carritoService.obtenerDetallesOrden().size();
	}

	// Listar todos los productos > a 0 unidades para la vista de los usuarios
	@GetMapping("/")
	private String home(Model model) {
		model.addAttribute("Categorias", categoriaService.listarCategorias());
		model.addAttribute("produ", productoService.findByCantidadGreaterThan(0));
		return "usuarios/home";
	}

	// Listar detalle de producto para el usuario
	@GetMapping("/Public/DetalleProducto/{id}")
	public String detalleProducto(@PathVariable Integer id, Model model) {

		Optional<Producto> producto = productoService.get(id);
		    model.addAttribute("produ", producto.get());
			return "usuarios/detalleproductos.html";
	}

	// Buscador de productos
	@GetMapping("/Public/BuscadorAjax")
	@ResponseBody
	public List<Producto> buscadorAjax(@RequestParam String nombreProducto) {
		return productoService.findAll().stream().filter(p -> p.getNombre().contains(nombreProducto))
				.collect(Collectors.toList());
	}

	// Buscador mediante categoria
	@GetMapping("/BuscarCategoriaAjax")
	@ResponseBody
	public List<Producto> buscarCategoriaAjax(@RequestParam("categoria") Integer categoriaId) {
		List<Producto> productos;
		if (categoriaId <= 0) {
			productos = productoService.findByCantidadGreaterThan(0);
		} else {
			productos = productoService.listarPorCategoria(categoriaId);
		}
		return productos;
	}

	// Listar los productos llevados al carro cuando se le da click al carro
	// flotante
	@GetMapping("/Public/MostrarCarro")
	public String mostrarCarro(Model model) {
		model.addAttribute("carro", carritoService.obtenerDetallesOrden());
		model.addAttribute("orden", carritoService.obtenerOrden());
		return "usuarios/mostrarcarro";
	}

	@PostMapping("/Public/AgregarCarroAjax")
	@ResponseBody
	public ResponseEntity<?> añadirCarroAjax(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam Integer cantidad, String pantalla) {
		try {
			Map<String, Object> response = carritoService.agregarProductoCarro(id, cantidad);
			String mensaje = (String) response.get("mensaje");
			// Para notificarle al usuario que ya no hay mas unidades para agregar al carro
			if (mensaje.contains("No hay stock")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay stock disponible");
			}
			return ResponseEntity.ok(response);
		} catch (ProductoNoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar producto al carrito");
		}
	}

	@GetMapping("/Public/EliminarProductoCarro/{idProducto}")
	@ResponseBody
	public Map<String, Object> eliminarProductoCarro(@PathVariable Integer idProducto) {
		return carritoService.eliminarProductoCarro(idProducto);
	}

	@GetMapping("/GuardarOrden")
	public String guardarOrden(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
		return ordenService.guardarOrden(userDetails);
	}

	@GetMapping("/ResumenOrden")
	public String resumenOrden(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
			Model model) {

		// Obtener el usuario actual desde la sesión
		Optional<Usuario> usuario = usuarioService.findByCorreo(userDetails.getUsername());

		if (usuario.isPresent()) {
			model.addAttribute("user", usuario.get());
			model.addAttribute("carro", carritoService.obtenerDetallesOrden());
			model.addAttribute("orden", carritoService.obtenerOrden());

			return "usuarios/ordenresumen";
		} else {
			model.addAttribute("Se he presentado un error");
			return "usuarios/error.html";
		}

	}

}
