package com.h.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h.Dao.IDetalleOrdenDao;
import com.h.entity.detalleOrden;
import com.h.service.IDetalleOrdenService;


@Service
public class DetalleOrdenServiceImpl  implements IDetalleOrdenService{

	
	//se hace la inyeccion para póder hacer uso de los metodos de JPA
	@Autowired
	private IDetalleOrdenDao detalleoDao;

    //Metodo para alamcenar el detalle de la orden
	@Override
	public detalleOrden guardar(detalleOrden detalleO) {
		
		return detalleoDao.save(detalleO);
	}

	
	
	@Override
	public List<detalleOrden> findByOrdenId(Integer ordenId) {
	    
	    return detalleoDao.findByOrdenId(ordenId);
	}


}
