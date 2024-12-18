package com.h.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ISubirImagenService {

	String GuardarImagen(MultipartFile file, String nuevoNombre) throws IOException;

	void BorrarImagen(String nombre);

}
