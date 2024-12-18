package com.h.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;


import com.h.entity.Producto;
import com.h.entity.Usuario;

public interface IProductoService {
	
	public Producto save(Producto producto ,Usuario usuario,MultipartFile file) throws IOException;
	//Lista por id
	public Optional <Producto> get(Integer id);
	
	public void update (Producto producto);
	
	public void delete (Integer id);
	
	//Encontrar producto por id
	public Optional<Producto> findById(Integer id);
	
	//Listar todos los productos que tengan mas de 1 unidad disponible
	public List<Producto> findByCantidadGreaterThan(int cantidad);
	
	//Metodo para traer todas la}os productos activos y   paginados
	Page<Producto> listarPaginadoActivos(PageRequest of);
	
	//Metodo para traer todas la}os productos No activos y   paginados
    Page<Producto> listarPaginadoNoActivos(PageRequest of);
	
	//Traer todos los productos
	List<Producto> findAll();
	
	
    //Traer un producto filtrado por nombre y paginado
	Page<Producto> listarPorNombre(String nombre, PageRequest pageRequest,String estadoproducto);
	
	//Trae los productos filtrados por categoria paginado
	Page<Producto> listarPorCategoriaPaginado(Integer id, PageRequest pageRequest,String estadoProducto);
	
	//Trae los productos filtrados por categoria sin paginar
	List<Producto> listarPorCategoria(Integer id);
	
	//Actualizar producto
	public boolean actualizarProducto(Producto producto, MultipartFile file) throws IOException;
	
	//desactiva un producto
	public void desactivarProducto(Integer id);
	
	//aactiva un producto
	public void activarProducto(Integer id);
	
	
	
	
	 
	
}
