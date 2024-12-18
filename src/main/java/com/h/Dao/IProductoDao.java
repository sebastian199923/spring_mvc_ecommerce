package com.h.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.h.entity.Producto;
import org.springframework.data.domain.Pageable;

@Repository
public interface IProductoDao extends JpaRepository<Producto, Integer> {

	// Listar productos donde la cantidad de productos disponibles sea mayor a 0
	List<Producto> findByCantidadGreaterThan(int cantidad);

	// Buscar producto por nombre
	// Page<Producto> findByNombreContainingIgnoreCase(String nombre, PageRequest
	// pageRequest);

	// Listar por nombre y estado activo
	@Query("SELECT p FROM Producto p WHERE p.activo = TRUE AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
	Page<Producto> findByNombreContainingIgnoreCaseAndActivos(@Param("nombre") String nombre, PageRequest pageable);

	// Listar por nombre y estado No activo
	@Query("SELECT p FROM Producto p WHERE p.activo = FALSE AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
	Page<Producto> findByNombreContainingIgnoreCaseAndNoActivos(@Param("nombre") String nombre, PageRequest pageable);

	// Listar por categoria y paginados
	@Query("SELECT p FROM Producto p WHERE p.activo = TRUE AND p.categoria.id = :categoriaId")
	Page<Producto> findByCategoriaIdActivos(@Param("categoriaId") Integer categoriaId, PageRequest pageable);

	// Listar por categoria y paginados
	@Query("SELECT p FROM Producto p WHERE p.activo = FALSE AND p.categoria.id = :categoriaId")
	Page<Producto> findByCategoriaIdNoActivos(@Param("categoriaId") Integer categoriaId, PageRequest pageable);

	// Listar por categoria sin paginar
	List<Producto> findByCategoriaId(Integer categoriaId);

	// Listar productos activos y paginados
	@Query("SELECT p FROM Producto p WHERE p.activo = TRUE")
	Page<Producto> productosActivosPaginados(Pageable pageable);

	// Listar productos No activos y paginados
	@Query("SELECT p FROM Producto p WHERE p.activo = FALSE")
	Page<Producto> productosNoActivosPaginados(Pageable pageable);

}
