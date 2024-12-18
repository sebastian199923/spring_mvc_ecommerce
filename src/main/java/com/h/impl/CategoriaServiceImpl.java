package com.h.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.h.Dao.ICategoriaDao;
import com.h.entity.Categoria;
import com.h.service.ICategoriaService;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

	@Autowired
	private ICategoriaDao categoriaDao;

	// Paginar los resultados de las ordenes
	@Override
	public Page<Categoria> listarPaginado(PageRequest pageable) {
		return categoriaDao.findAll(pageable);
	}

	// Guardar nuevas categorias
	@Override
	public Categoria save(Categoria categoria) {

		return categoriaDao.save(categoria);
	}

	// Buscar categorias mediante el nombre
	@Override
	public Page<Categoria> listarPorNombre(String nombreCategoria, PageRequest pageRequest) {

		return categoriaDao.findByNombreCategoriaContainingIgnoreCase(nombreCategoria, pageRequest);
	}

	// Obtener categoria por su id
	@Override
	public Optional<Categoria> get(Integer id) {

		return categoriaDao.findById(id);
	}

	// Eliminar categoria por su id
	@Override
	public void delete(Integer id) {
		categoriaDao.deleteById(id);

	}

	// Editar una categoria
	@Override
	public void update(Categoria categoria) {
		categoriaDao.save(categoria);

	}

	@Override
	public List<Categoria> listarCategorias() {

		return categoriaDao.findAll();
	}

}
