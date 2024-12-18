package com.h.service;

import java.io.IOException;
import java.util.List;

import com.h.entity.detalleOrden;

public interface IPDFService {
	
	
	public	byte[] generateStyledPdf(List<detalleOrden> detalleOrden, String numeroOrden, String nombreUsuario, String fechaCompra) throws IOException;
	

	

	


}
