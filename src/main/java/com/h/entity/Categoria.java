package com.h.entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="categorias")
public class Categoria {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	private Integer id;
	private String nombreCategoria;
	
	
	
	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Set<Producto> productos = new HashSet<>();
	
	
	
	
	
	public Categoria() {
		super();
	}
	
	
	
	
	




	public Categoria(Integer id, String nombreCategoria, Set<Producto> productos) {
		super();
		this.id = id;
		this.nombreCategoria = nombreCategoria;
		this.productos = productos;
	}









	public Integer getId() {
		return id;
	}
	public String getNombreCategoria() {
		return nombreCategoria;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}






	public Set<Producto> getProductos() {
		return productos;
	}






	public void setProductos(Set<Producto> productos) {
		this.productos = productos;
	}
	
	
	




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
