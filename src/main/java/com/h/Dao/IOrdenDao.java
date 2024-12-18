package com.h.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.h.entity.Orden;
import com.h.entity.Producto;
import com.h.entity.Usuario;

@Repository
public interface IOrdenDao extends JpaRepository<Orden,Integer> {

    ///Metodo que  retorna la ordenes que tiene determinado usuario
	List<Orden>  findByUsuario (Usuario user);
	
	
	//Buscar orden por numero
	Page<Orden> findByNumeroContainingIgnoreCase(String numero, PageRequest pageRequest);
	
	
}
