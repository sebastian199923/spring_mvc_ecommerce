package com.h.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="ordenes")
public class Orden {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	private Integer id;
	private String numero;
	private Date fechaCreacion;
	private double total;
	
	
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	@JsonIgnoreProperties("orden")
	private Usuario usuario;
	
    public Orden() {
		super();
	}

    
    public Orden(Integer id, String numero, Date fechaCreacion,  double total, Usuario usuario) {
		super();
		this.id = id;
		this.numero = numero;
		this.fechaCreacion = fechaCreacion;
	
		this.total = total;
		this.usuario = usuario;
	}
    

    public Integer getId() {
		return id;
	}

	public String getNumero() {
		return numero;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	

	public double getTotal() {
		return total;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	

	public void setTotal(double total) {
		this.total = total;
	}
	
	
	

	public Usuario getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

    @Override
	public String toString() {
		return "Orden [id=" + id + ", numero=" + numero + ", fechaCreacion=" + fechaCreacion +  ", total=" + total + "]";
	}
	
	
	
	
	
	

}
