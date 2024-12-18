package com.h.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.h.entity.Categoria;
import com.h.entity.Producto;
import com.h.entity.Usuario;
import com.h.service.ICategoriaService;
import com.h.service.IProductoService;
import com.h.service.IUsuarioService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("Productos")
public class ProductoController {

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService userservice;

	@Autowired
	private ICategoriaService categoriaService;

	Random random = new Random();

	// Listar productos activos paginado para el admin
	@GetMapping("/Listar")
	public String ListarProductos(@RequestParam(defaultValue = "0") int page, Model model) {
		PageRequest paginado = PageRequest.of(page, 10);
		Page<Producto> productos = productoService.listarPaginadoActivos(paginado);
		List<Categoria> catego = categoriaService.listarCategorias();
		model.addAttribute("Categorias", catego);
		model.addAttribute("Productos", productos);
		return "Productos/Listar";
	}

	// Listar paginado para el admin los productos inactivos
	@GetMapping("/ListarDesactivados")
	public String ListarProductosDesactivados(@RequestParam(defaultValue = "0") int page, Model model) {
		PageRequest paginado = PageRequest.of(page, 10);
		Page<Producto> productos = productoService.listarPaginadoNoActivos(paginado);
		List<Categoria> catego = categoriaService.listarCategorias();
		model.addAttribute("Categorias", catego);
		model.addAttribute("Productos", productos);
		return "Productos/ListarDesactivados";
	}

	@PostMapping("/Guardar")
	public ResponseEntity<Producto> Crear(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
			RedirectAttributes redirectAttributes, Producto producto, @RequestParam("img") MultipartFile file)
			throws Exception {

		Optional<Usuario> usuario = userservice.findByCorreo(userDetails.getUsername());

		if (usuario.isPresent()) {

			productoService.save(producto, usuario.get(), file);
			return ResponseEntity.ok(producto);
		} else {
			//
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	/// Listar Producto a editar
	@GetMapping("/Editar/{id}")
	public String Editar(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();
		producto.setPrecioFormateado(String.format("%.0f", producto.getPrecio()));
		model.addAttribute("Categorias", categoriaService.listarCategorias());
		model.addAttribute("Producto", producto);
		return "productos/Editar";

	}

	@GetMapping("/DesactivarProducto/{id}")
	public ResponseEntity<String> desactivarProducto(@PathVariable Integer id) {
		try {
			// Llamar al servicio para eliminar el producto
			productoService.desactivarProducto(id);
			return ResponseEntity.ok("Producto desactivado exitosamente");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Error al desactivar el producto: " + e.getMessage());
		}
	}

	@GetMapping("/activarProducto/{id}")
	public ResponseEntity<String> activarProducto(@PathVariable Integer id) {
		try {
			// Llamar al servicio para activar el producto
			productoService.activarProducto(id);
			return ResponseEntity.ok("Producto activado exitosamente");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Error al desactivar el producto: " + e.getMessage());
		}
	}

	@PostMapping("/Actualizar")
	public String Actualizar(RedirectAttributes redirectAttributes, Producto producto,
			@RequestParam("img") MultipartFile file) throws IOException {

		boolean actualizar = productoService.actualizarProducto(producto, file);

		// Verificar si la actualización fue exitosa
		if (actualizar) {
			redirectAttributes.addFlashAttribute("mensajeAlerta", "El producto ha sido actualizado");
			redirectAttributes.addFlashAttribute("tipoAlerta", "success");
		} else {
			redirectAttributes.addFlashAttribute("mensajeAlerta", "Hubo un error al actualizar el producto");
			redirectAttributes.addFlashAttribute("tipoAlerta", "danger");
		}
		return "redirect:/Productos/Listar";
	}

	@GetMapping("/BuscarCategoria")
	public ResponseEntity<Page<Producto>> buscarCategoria(@RequestParam(defaultValue = "0") int page,
			@RequestParam("categoria") Integer categoriaId, @RequestParam("estadoProducto") String estadoProducto) {

		PageRequest paginado = PageRequest.of(page, 10);
		Page<Producto> productos;

		// Lista todas las categorias
		if (categoriaId <= 0) {
			// 0 == productos inactivos
			if (estadoProducto.equals("0")) {
				productos = productoService.listarPaginadoNoActivos(paginado);
			} else {
				productos = productoService.listarPaginadoActivos(paginado);
			}

		} else {

			productos = productoService.listarPorCategoriaPaginado(categoriaId, paginado, estadoProducto);
		}
		return ResponseEntity.ok(productos);
	}

	@PostMapping("/BuscarProductoAjax")
	@ResponseBody
	public Map<String, Object> buscarProductoAjax(@RequestParam String nombreProducto,
			@RequestParam String estadoProducto, @RequestParam(defaultValue = "0") int page) {

		PageRequest paginado = PageRequest.of(page, 10);
		Page<Producto> productos = productoService.listarPorNombre(nombreProducto, paginado, estadoProducto);
		List<Categoria> categorias = categoriaService.listarCategorias();

		Map<String, Object> response = new HashMap<>();
		response.put("productos", productos.getContent());
		response.put("totalPages", productos.getTotalPages());
		response.put("currentPage", productos.getNumber());
		response.put("categorias", categorias);
		return response;
	}

	@DeleteMapping("/Eliminar/{id}")
	public ResponseEntity<String> eliminarProducto(@PathVariable Integer id) {
		try {
			// Llamar al servicio para eliminar el producto
			productoService.delete(id);
			return ResponseEntity.ok("Producto eliminado exitosamente");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al eliminar el producto: " + e.getMessage());
		}
	}

}
