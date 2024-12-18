package com.h.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.h.entity.Orden;
import com.h.service.IOrdenService;

@Controller
@RequestMapping("/Orden")
public class OrdenController {

	@Autowired
	private IOrdenService ordenService;

	// Metodo para listar todas las ordenes que se han generado en sistema
	@GetMapping("/TodasCompras")
	public String listarCompras(@RequestParam(defaultValue = "0") int page, Model model) {
		PageRequest pageRequest = PageRequest.of(page, 10);
		Page<Orden> ordenes = ordenService.listarPaginado(pageRequest);
		model.addAttribute("ordenes", ordenes);
		return "Admin/todascompras";
	}

	@PostMapping("/BuscarOrdenAjax")
	@ResponseBody
	public List<Orden> buscarOrdenAjax(@RequestParam String numeroOrden, @RequestParam(defaultValue = "0") int page) {
		PageRequest paginado = PageRequest.of(page, 10);

		Page<Orden> ordenes;
		if (numeroOrden.equals("")) {
			ordenes = ordenService.listarPaginado(paginado);
	    } else {
			ordenes = ordenService.listarPorNumero(numeroOrden, paginado);
		}

		return ordenes.getContent(); // devuelve solo la lista de órdenes, no la página completa
	}

}
