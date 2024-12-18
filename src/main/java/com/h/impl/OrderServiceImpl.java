package com.h.impl;
import com.h.entity.detalleOrden;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import com.h.Dao.IOrdenDao;
import com.h.entity.Orden;
import com.h.entity.Usuario;
import com.h.service.ICarritoService;
import com.h.service.IDetalleOrdenService;
import com.h.service.IEmailService;
import com.h.service.IOrdenService;
import com.h.service.IProductoService;
import com.h.service.IUsuarioService;
import com.h.entity.Producto;

@Repository
public class OrderServiceImpl implements IOrdenService {

	/// Se hace la inyeccion para usar los metodos de JPA
	@Autowired
	private IOrdenDao ordenService;

	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	@Autowired
	private IEmailService emailService;
	
	@Autowired
	private ICarritoService carritoService;

	

	/// Metodo para guardar la orden cuando el usuario ya lo haya confirmado
	@Override
	public Orden guardar(Orden orden) {
     return ordenService.save(orden);
	}

	/// Listar todas las ordenes
	@Override
	public List<Orden> listarOrdenes() {
		return ordenService.findAll();
	}

	/// metodo que genera el numero de orden
	public String generaOrden() {
		int num = 0;
		String numConcate = "";
        List<Orden> ordenes = listarOrdenes();
		List<Integer> numeros = new ArrayList<Integer>();
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
        if (ordenes.isEmpty()) {
			num = 1;
		} else {
			num = numeros.stream().max(Integer::compare).get();
			num++;
		}

		if (num < 10) {
			numConcate = "00000000" + String.valueOf(num);
		} else if (num < 100) {
			numConcate = "0000000" + String.valueOf(num);
		} else if (num < 1000) {
			numConcate = "000000" + String.valueOf(num);
		}

		return numConcate;
	}

	// Buscar una orden por usuario
	@Override
	public List<Orden> findByUsuario(Usuario user) {

		return ordenService.findByUsuario(user);
	}

	/// Busca 1 orden segun el id que se le pase
	@Override
	public Optional<Orden> findById(Integer id) {

		return ordenService.findById(id);
	}

	// Paginar los resultados de las ordenes
	@Override
	public Page<Orden> listarPaginado(PageRequest pageable) {
		return ordenService.findAll(pageable);
	}

	// Trae una orden filtrada por numero y paginada
	@Override
	public Page<Orden> listarPorNumero(String numero, PageRequest pageRequest) {
		// findByNombreContainingIgnoreCase  buscar sin importar mayúsculas o minúsculas
		return ordenService.findByNumeroContainingIgnoreCase(numero, pageRequest);
	}

	// Método para guardar una orden
    public String guardarOrden(org.springframework.security.core.userdetails.User userDetails) {
        try {
            Orden orden = carritoService.obtenerOrden();
        	Date fechaOrden = new Date();
            orden.setFechaCreacion(fechaOrden);
            orden.setNumero(generaOrden());
            // Obtener usuario desde sesión
            Optional<Usuario> usuario = usuarioService.findByCorreo(userDetails.getUsername());
            orden.setUsuario(usuario.get());
            guardar(orden);
            //Obtiene la lista de detalles de ordenes
            List<detalleOrden> detalleOrden = carritoService.obtenerDetallesOrden();
            // Guardar detalles de la orden
            for (detalleOrden dt : detalleOrden) {
                dt.setOrden(orden);
                detalleOrdenService.guardar(dt);
            }
            
            // Actualizar la cantidad de productos
            for (detalleOrden dt : detalleOrden) {
                Integer idProducto = dt.getProducto().getId();
                Producto producto = productoService.findById(idProducto)
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + idProducto));
                int nuevaCantidad = (int) (producto.getCantidad() - dt.getCantidad());
                producto.setCantidad(nuevaCantidad);
                productoService.update(producto);
            }
            // Enviar correo electrónico con la factura
             enviarFacturaEmail(orden, detalleOrden);
             // Limpiar la orden y el carrito
            carritoService.limpiarOrdenCarrito();
        return "redirect:/"; // Redireccionar a la página principal

        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Retornar un error si algo falla
        }
    }
    //Enviar factura por correo electronico
    private void enviarFacturaEmail(Orden orden, List<detalleOrden> detalleOrden) throws Exception {
        SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy MMMM dd HH:mm", new Locale("es", "ES"));
        String fechaFormateada = formatoSalida.format(orden.getFechaCreacion());
        String nombreCompleto = orden.getUsuario().getNombre() + " " + orden.getUsuario().getApellido();
        emailService.sendEmailWithPdf(orden.getUsuario().getCorreo(), nombreCompleto, detalleOrden,
                orden.getNumero(), fechaFormateada);
    }
}
