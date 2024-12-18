package com.h.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String apellido;
	@Column(name = "correo", nullable = false, unique = true)
	private String correo;
	private String direccion;
	private String telefono;
	private String tipo;
	@Size(min = 8)
	private String contrasena;
	@Column(name = "cedula", nullable = false, unique = true)
	private String cedula;

	private LocalDateTime ultimoLogin;

	// Usuario puede tener muchos productos
	@OneToMany(mappedBy = "usuario")
	@JsonManagedReference
	private List<Producto> producto;

	// Usuario puede tener muchas ordenes
	@OneToMany(mappedBy = "usuario")
	@JsonManagedReference
	private List<Orden> orden;

	// Constructor vacio
	public Usuario() {
		super();
	}

	public Usuario(Integer id, String nombre, String apellido, String correo, String direccion, String telefono,
			String tipo, @Size(min = 8) String contrasena, String cedula, LocalDateTime ultimoLogin,
			List<Producto> producto, List<Orden> orden) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.correo = correo;
		this.direccion = direccion;
		this.telefono = telefono;
		this.tipo = tipo;
		this.contrasena = contrasena;
		this.cedula = cedula;
		this.ultimoLogin = ultimoLogin;
		this.producto = producto;
		this.orden = orden;
	}

	public Usuario(Integer integer, String string, Double double1, Long long1) {
		// TODO Auto-generated constructor stub
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public List<Producto> getProducto() {
		return producto;
	}

	public void setProducto(List<Producto> producto) {
		this.producto = producto;
	}

	public String getCedula() {
		return cedula;
	}

	public List<Orden> getOrden() {
		return orden;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public void setOrden(List<Orden> orden) {
		this.orden = orden;
	}

	public LocalDateTime getUltimoLogin() {
		return ultimoLogin;
	}

	public void setUltimoLogin(LocalDateTime ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}
	
	
	
	

}
