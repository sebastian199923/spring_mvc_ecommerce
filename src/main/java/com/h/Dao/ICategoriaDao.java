package com.h.Dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.h.entity.Categoria;
import com.h.entity.Orden;
import com.h.entity.Producto;


public interface ICategoriaDao extends JpaRepository<Categoria, Integer> {
	
	
	//Buscar producto por nombre
	Page<Categoria> findByNombreCategoriaContainingIgnoreCase(String nombreCategoria, PageRequest pageRequest);

	

}
