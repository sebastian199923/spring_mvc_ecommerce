package com.h.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;

@Entity
@Table(name="detalleOrden")
public class detalleOrden {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	private Integer id;
	private String nombre;
	private double cantidad;
	private double precio;
	private double total;
	
	
	@ManyToOne
	@JoinColumn(name = "orden_id", nullable = false) 
	private Orden orden;
	
	@ManyToOne
	@JsonBackReference
	private Producto producto;
	
	public detalleOrden() {
		super();
	}


	

	public detalleOrden(Integer id, String nombre, double cantidad, double precio, double total, Orden orden,
			Producto producto) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.precio = precio;
		this.total = total;
		this.orden = orden;
		this.producto = producto;
	}




	public Integer getId() {
		return id;
	}


	public String getNombre() {
		return nombre;
	}


	public double getCantidad() {
		return cantidad;
	}


	public double getPrecio() {
		return precio;
	}


	public double getTotal() {
		return total;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}


	public void setTotal(double total) {
		this.total = total;
	}
	
	
	


	public Orden getOrden() {
		return orden;
	}


	public Producto getProducto() {
		return producto;
	}


	public void setOrden(Orden orden) {
		this.orden = orden;
	}


	public void setProducto(Producto producto) {
		this.producto = producto;
	}


	@Override
	public String toString() {
		return "detalleOrden [id=" + id + ", nombre=" + nombre + ", cantidad=" + cantidad + ", precio=" + precio
				+ ", total=" + total + "]";
	}
	
	
	
	
	
	
	
	

}
