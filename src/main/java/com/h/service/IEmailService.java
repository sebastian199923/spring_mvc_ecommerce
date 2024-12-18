package com.h.service;

import java.sql.Date;
import java.util.List;

import com.h.entity.detalleOrden;

public interface IEmailService {
	
	public void sendEmailfForgottenPassword(String correo,String numeroRandom ,String nombreUsuario);
    public void sendEmailWithPdf(String correo, String nombreUsuario, List<detalleOrden> detalleOrden,String numeroOrden,String fechaCompra);

}
