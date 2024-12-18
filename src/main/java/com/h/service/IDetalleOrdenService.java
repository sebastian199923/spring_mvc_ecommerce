package com.h.service;

import java.util.List;

import com.h.entity.detalleOrden;

public interface IDetalleOrdenService {
	
	detalleOrden guardar(detalleOrden detalleO);
	//Lista los detallesOrden filtrando por ordenId
	List<detalleOrden>  findByOrdenId (Integer detalleO);

}
