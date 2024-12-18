package com.h.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.h.entity.Orden;
import com.h.entity.Producto;
import com.h.entity.detalleOrden;
import com.h.exception.ProductoNoEncontradoException;
import com.h.service.ICarritoService;
import com.h.service.IProductoService;

@Service
public class CarritoServiceImpl implements ICarritoService {

	@Autowired
	private IProductoService productoService;

	// Guardar detalles de orden
	List<detalleOrden> detalleOrden = new ArrayList<detalleOrden>();

	// datos de orden
	Orden orden = new Orden();

	// agregar producto al carro
	@Override
	public Map<String, Object> agregarProductoCarro(Integer id, Integer cantidad) {
		// Obtiene el producto
		Optional<Producto> productoOptional = productoService.get(id);
		if (productoOptional.isEmpty()) {
			throw new ProductoNoEncontradoException();
		}

		Producto producto = productoOptional.get();
		double stockRestante = producto.getCantidad() - cantidad;
		// Valida si la cantidad que el usuario pide si esta dispobible
		if (stockRestante < 0) {
			return Map.of("mensaje", "No hay stock disponible");
		}

		stockRestante = producto.getCantidad();
		Optional<detalleOrden> productoExistente = detalleOrden.stream()
				.filter(detalle -> detalle.getProducto().getId().equals(id)).findFirst();

		// valida si ya existe el producto en el carrito
		if (productoExistente.isPresent()) {

			detalleOrden detalle = productoExistente.get();
			stockRestante = producto.getCantidad() - (detalle.getCantidad() + cantidad);
			// Vaalida si en el carro hay mas productos de los que hay disponibles
			if (stockRestante < 0) {
				return Map.of("mensaje", "No hay stock disponible");
			}
			detalle.setCantidad(detalle.getCantidad() + cantidad);
			detalle.setTotal(producto.getPrecio() * detalle.getCantidad());
		} else {
			detalleOrden nuevoDetalle = new detalleOrden();
			nuevoDetalle.setProducto(producto);
			nuevoDetalle.setCantidad(cantidad);
			nuevoDetalle.setNombre(producto.getNombre());
			nuevoDetalle.setPrecio(producto.getPrecio());
			nuevoDetalle.setTotal(producto.getPrecio() * cantidad);
			detalleOrden.add(nuevoDetalle);
		}
		// calcular total del carrito
		double totalCarrito = detalleOrden.stream().mapToDouble(det -> det.getTotal()).sum();
		orden.setTotal(totalCarrito);
		// Retornar datos
		return Map.of("mensaje", "Producto agregado al carrito", "total", totalCarrito, "cantidadProductos",
				detalleOrden.size());
	}

	// Método que retorna el contenido de la lista del carrito
	public List<detalleOrden> obtenerDetallesOrden() {
		return detalleOrden;
	}

	// Retorna la orden actual
	public Orden obtenerOrden() {
		return orden;
	}

	// Limpiar carrito
	@Override
	public void limpiarOrdenCarrito() {
		
		orden = new Orden(); 
		detalleOrden.clear(); 
	}

	// Método para eliminar producto del carrito y recalcular el total
	public Map<String, Object> eliminarProductoCarro(Integer idProducto) {
		try {

			List<detalleOrden> nuevaOrden = new ArrayList<>();
			for (detalleOrden ordenD : detalleOrden) {
				if (!ordenD.getProducto().getId().equals(idProducto)) {
					nuevaOrden.add(ordenD);
				}
			}
			// Actualiza la lista del carrito
			detalleOrden = nuevaOrden;
            // Recalcular el total de la orden
			double sumaDeProductos = detalleOrden.stream().mapToDouble(det -> det.getTotal()).sum();
			orden.setTotal(sumaDeProductos);

			// Crear la respuesta JSON
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Producto eliminado del carrito");
			response.put("total", sumaDeProductos);

			return response;
		} catch (Exception e) {
			System.err.println("Error al procesar la eliminación del producto: " + e.getMessage());
			e.printStackTrace();

			// Crear respuesta JSON de error
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "Ocurrió un error al eliminar el producto del carrito");
			errorResponse.put("error", e.getMessage());
			return errorResponse;
		}
	}

}
