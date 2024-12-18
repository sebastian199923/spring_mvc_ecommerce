package com.h.service;
import java.util.List;
import java.util.Map;
import com.h.entity.Orden;
import com.h.entity.detalleOrden;

public interface ICarritoService {

	// Limpia el carrito y la orden despues de haber hecho la compra
	void limpiarOrdenCarrito();

	// Agregar productos al carrito
	Map<String, Object> agregarProductoCarro(Integer id, Integer cantidad);

	// Método que retorna el contenido de la lista del carrito
	public List<detalleOrden> obtenerDetallesOrden();

	// Retorna la orden actual
	public Orden obtenerOrden();

	//Eliminar productos del carrito
	Map<String, Object> eliminarProductoCarro(Integer idProducto);

}
