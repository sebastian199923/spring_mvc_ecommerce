package com.h.controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.h.entity.Categoria;
import com.h.service.ICategoriaService;

@Controller
@RequestMapping("/Categorias")
public class CategoriaController {

	@Autowired
	private ICategoriaService categoriaService;

	// Listar todas las categorias
	@GetMapping("/ListarCategorias")
	public String listarCategorias(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest paginado = PageRequest.of(page, 10);
		Page<Categoria> categorias = categoriaService.listarPaginado(paginado);
        model.addAttribute("Categorias", categorias);
		return "Admin/listarcategorias";
	}

    @PostMapping("/GuardarCategoria")
	public ResponseEntity<Categoria> guardarCategoria(Categoria categoria)  {
		   categoriaService.save(categoria);
	       return ResponseEntity.ok(categoria); 
	   }


	@PostMapping("/BuscarCategoriaAjax")
	@ResponseBody
	public Map<String, Object> buscarCategoriaAjax(@RequestParam String nombreCategoria, 
	                                              @RequestParam(defaultValue = "0") int page) {
		PageRequest paginado = PageRequest.of(page, 10);
	    Page<Categoria> categorias = categoriaService.listarPorNombre(nombreCategoria, paginado);
	    Map<String, Object> response = new HashMap<>();
	    response.put("categorias", categorias.getContent());
	    response.put("totalPages", categorias.getTotalPages());
	    response.put("currentPage", categorias.getNumber());
	    return response;
	}
	
	@DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Integer id) {
	    categoriaService.delete(id);
        return ResponseEntity.ok("Categoria eliminada");
    }
  

	// Editar categoria
	@GetMapping("/Editar/{id}")
	public String Editar(@PathVariable Integer id, Model model) {
        Categoria categoria = new Categoria();
        Optional<Categoria> optionalCategoria = categoriaService.get(id);
        categoria = optionalCategoria.get();
        model.addAttribute("Categoria", categoria);
		return "Admin/editarcategoria";
	}
	
	// Editar
	@PostMapping("/Actualizar")
	public String Actualizar(RedirectAttributes redirectAttributes, Categoria categoria) {
		categoriaService.update(categoria);
		redirectAttributes.addFlashAttribute("mensajeAlerta", "La categoria ha sido actualizado");
		redirectAttributes.addFlashAttribute("tipoAlerta", "success");
		return "redirect:/Categorias/ListarCategorias";

	}
}
