package com.h.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.h.entity.Orden;
import com.h.entity.Usuario;
import com.h.entity.detalleOrden;

@Repository
public interface IDetalleOrdenDao extends JpaRepository<detalleOrden,Integer> {
	
	List<detalleOrden>  findByOrdenId (Integer detalleO);

}
