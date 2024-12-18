package com.h.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.h.service.ISubirImagenService;

@Service
public class SubirImagenServiceImpl  implements ISubirImagenService{

	private String carpeta = "imagenes-productos//";

	@Override
	public String GuardarImagen(MultipartFile file, String nuevoNombre) throws IOException {
		if (!file.isEmpty()) {

			String originalFilename = file.getOriginalFilename();
			String extension = "";
			if (originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			String nombreArchivo;
			if (nuevoNombre != null && !nuevoNombre.isEmpty()) {

				nombreArchivo = nuevoNombre + extension;
			} else {

				nombreArchivo = originalFilename;
			}
			Path path = Paths.get(carpeta + nombreArchivo);
			byte[] bytes = file.getBytes();
			Files.write(path, bytes);
			return nombreArchivo;
		} else {
			String nombreArchivoPorDefecto = "porDefecto.png";
			return nombreArchivoPorDefecto;
		}
	}

	@Override
	public void BorrarImagen(String nombre) {
		String ruta = "imagenes-productos//";
		File file = new File(ruta + nombre);
		if (file.getName().equals("porDefecto.png")) {

		} else {
			file.delete();
		}

	}

}
