package com.h.service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.h.entity.Categoria;
import com.h.entity.Producto;



public interface ICategoriaService {

	//Metodo para traer todas las ordenes paginadas 
	Page<Categoria> listarPaginado(PageRequest of);

	//Guardar nuevas categorias
	Categoria save(Categoria categoria);

	//Listar categorias por nombre paginadas
	Page<Categoria> listarPorNombre(String nombreCategoria, PageRequest pageRequest);
	
	
	//Obtener 1 categoria por su id
	public Optional <Categoria> get(Integer id);
	
	
	//Eliminar categoria
	public void delete (Integer id);

	
	//Editar categoria
	public void update(Categoria categoria);
	
	
	//Metodo para traer todas las ordenes paginadas 
    List<Categoria> listarCategorias();
    
	
	

}
