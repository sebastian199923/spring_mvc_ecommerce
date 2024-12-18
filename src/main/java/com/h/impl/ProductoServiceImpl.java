package com.h.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.h.Dao.IProductoDao;
import com.h.entity.Producto;
import com.h.entity.Usuario;
import com.h.service.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService {

	@Autowired
	private IProductoDao productoDao;

	Random random = new Random();

	@Autowired
	private SubirImagenServiceImpl subirimagen;

	@Override
	public Producto save(Producto producto, Usuario usuario, MultipartFile file) throws IOException {

		producto.setUsuario(usuario);
		// Cargue de imagens
		if (producto.getId() == null) {
			int numeroAleatorio = random.nextInt(101);
			String nombreProducto = producto.getNombre() + numeroAleatorio;
			String imagenN = subirimagen.GuardarImagen(file, nombreProducto);
			producto.setImagen(imagenN);
		}
		producto.setActivo(true);
		return productoDao.save(producto);
	}

	//Obener producto mediante su id
	@Override
	public Optional<Producto> get(Integer id) {

		return productoDao.findById(id);
	}
	

	@Override
	public void update(Producto producto) {
		productoDao.save(producto);

	}

	@Override
	public void delete(Integer id) {
		Optional<Producto> producto = findById(id);

		if (producto.isPresent()) {
			// Elimina la imagen
			if (producto.get().getImagen() != null && !producto.get().getImagen().equals("porDefecto.jpg")) {
				subirimagen.BorrarImagen(producto.get().getImagen());
			}
		}

		productoDao.deleteById(id);

	}

	public void desactivarProducto(Integer id) {
		Optional<Producto> producto = findById(id);

		if (producto.isPresent()) {
			Producto produ = producto.get();
			produ.setActivo(false);
		}

		productoDao.save(producto.get());

	}
	
	
	
	public void activarProducto(Integer id) {
		Optional<Producto> producto = findById(id);

		if (producto.isPresent()) {
			Producto produ = producto.get();
			produ.setActivo(true);
		}

		productoDao.save(producto.get());

	}

	@Override
	public List<Producto> findAll() {

		return productoDao.findAll();
	}

	@Override
	public Optional<Producto> findById(Integer id) {

		return productoDao.findById(id);
	}

	@Override
	public List<Producto> findByCantidadGreaterThan(int cantidad) {

		return productoDao.findByCantidadGreaterThan(cantidad);
	}

	// Traer todos los productos paginados y que esten activos
	public Page<Producto> listarPaginadoActivos(PageRequest pageable) {
		return productoDao.productosActivosPaginados(pageable);

	}

	// Traer todos los productos paginados y que esten activos
	public Page<Producto> listarPaginadoNoActivos(PageRequest pageable) {
		return productoDao.productosNoActivosPaginados(pageable);

	}

	// Buscar un producto mediante el nombre
	@Override
	public Page<Producto> listarPorNombre(String nombre, PageRequest pageRequest,String estadoproducto) {
		// findByNombreContainingIgnoreCase ignora si es MAYUS minus
		
		//Decide si lista productos activos o inactivos
		if(estadoproducto.equals("1")) {
			return productoDao.findByNombreContainingIgnoreCaseAndActivos(nombre, pageRequest);
		}else {
			return productoDao.findByNombreContainingIgnoreCaseAndNoActivos(nombre, pageRequest);	
		}
		
	}

	// Buscar un producto mediante la categoria y paginado
	@Override
	public Page<Producto> listarPorCategoriaPaginado(Integer idCategoria, PageRequest pageRequest,String estadoProducto) {

		//Decide si lista productos activos o inactivos
		if(estadoProducto.equals("0")) {
			return productoDao.findByCategoriaIdNoActivos(idCategoria, pageRequest);
		}else{
			return productoDao.findByCategoriaIdActivos(idCategoria, pageRequest);
		}
		
	}

	// Buscar productos mediante la categoria sin paginar
	@Override
	public List<Producto> listarPorCategoria(Integer id) {

		return productoDao.findByCategoriaId(id);
	}

	// Logica para actualizacion de producto
	public boolean actualizarProducto(Producto producto, MultipartFile file) throws IOException {
		// Recuperar el producto actual de la base de datos
		Optional<Producto> productoExistente = productoDao.findById(producto.getId());

		if (productoExistente.isPresent()) {
			Producto produ = productoExistente.get();

			if (file.isEmpty()) {
				producto.setImagen(produ.getImagen());
			} else {

				if (!produ.getImagen().equals("porDefecto.jpg")) {
					subirimagen.BorrarImagen(produ.getImagen());

				}

				int numeroAleatorio = random.nextInt(101);
				String nombreProducto = producto.getNombre() + String.valueOf(numeroAleatorio);
				String imagenN = subirimagen.GuardarImagen(file, nombreProducto);
				producto.setImagen(imagenN);
			}

			producto.setUsuario(produ.getUsuario());
			productoDao.save(producto);
			return true;
		}
		return false;
	}

}
