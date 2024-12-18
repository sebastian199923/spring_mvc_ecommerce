package com.h.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.h.entity.Orden;
import com.h.entity.Usuario;

public interface IOrdenService {
	
	//Metodo que sirve para generar el numero de orden
	String generaOrden();
	//Guarda las ordenes
	Orden guardar(Orden orden);
	//Obtiene todas las ordenes
	List<Orden> listarOrdenes();
	///Metodo que me retorna la ordenes que tiene determinado usuario
	List<Orden>  findByUsuario (Usuario user);
	//Obtiene una orden en especifico segun el id
	Optional<Orden> findById(Integer id);
	//Paginar ordenes
	Page<Orden> listarPaginado(PageRequest of);
	//Trae una orden filtrada por numero y paginada
    Page<Orden> listarPorNumero(String numero, PageRequest pageRequest);
    //Guardar Orden
     String guardarOrden(org.springframework.security.core.userdetails.User userDetails);
}
