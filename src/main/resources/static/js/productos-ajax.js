function actualizarTablaActivos(productos) {
    const tablaCuerpo = document.querySelector(".table tbody");
    tablaCuerpo.innerHTML = ""; // Limpia el contenido actual de la tabla

    productos.forEach(producto => {
        const fila = document.createElement("tr");
        // Asigna el id dinámicamente
        fila.id = `producto-${producto.id}`;
        fila.innerHTML = `
            <td>${producto.nombre}</td>
            <td>${producto.descripcion}</td>
            <td>${producto.categoria.nombreCategoria}</td>
            <td>${producto.cantidad}</td>
            <td>${'$' + producto.precio.toLocaleString("es-ES")}</td>
            <td>
                <img src="/imagenes-productos/${producto.imagen}" 
                     alt="Product Thumbnail" 
                     class="tab-image" 
                     style="width: 60px; height: 60px;">
            </td>
            <td>
                <a class="btn btn-warning" href="/Productos/Editar/${producto.id}">Editar</a>
            </td>
            
             <td>
                <button class="btn btn-secondary" 
                        type="button" 
                        onclick="desactivarProducto(${producto.id})">
                    Desactivar
                </button>
            </td>
            
            <td>
                <button class="btn btn-danger btn-eliminar" 
                        type="button" 
                        onclick="eliminarProducto(${producto.id})">
                    Eliminar
                </button>
            </td>
        `;
        tablaCuerpo.appendChild(fila);
    });
}

function actualizarTablaNoActivos(productos) {
    const tablaCuerpo = document.querySelector(".table tbody");
    tablaCuerpo.innerHTML = ""; // Limpia el contenido actual de la tabla

    productos.forEach(producto => {
        const fila = document.createElement("tr");
        // Asigna el id dinámicamente
        fila.id = `producto-${producto.id}`;
        fila.innerHTML = `
            <td>${producto.nombre}</td>
            <td>${producto.descripcion}</td>
            <td>${producto.categoria.nombreCategoria}</td>
            <td>${producto.cantidad}</td>
            <td>${'$' + producto.precio.toLocaleString("es-ES")}</td>
            <td>
                <img src="/imagenes-productos/${producto.imagen}" 
                     alt="Product Thumbnail" 
                     class="tab-image" 
                     style="width: 60px; height: 60px;">
            </td>
            
            
             <td>
                <button class="btn btn-secondary" 
                        type="button" 
                        onclick="activarProducto(${producto.id})">
                    Activar
                </button>
            </td>
            
           
        `;
        tablaCuerpo.appendChild(fila);
    });
}

//Funcion encargada del registro de producto
function registrarProducto(event) {
    event.preventDefault();
    const form = document.getElementById('formAgregarProducto');
    if (!form.checkValidity()) {
        form.reportValidity(); 
        return;
        }
    const formData = new FormData(form);
    fetch('/Productos/Guardar', {
        method: 'POST',
        body: formData,
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error en la solicitud: ${response.statusText} (Código de estado: ${response.status})`);
        }
        return response.json(); 
    })
    
    .then(producto => {
       
        $('#agregarProductoModal').modal('hide');
        form.reset();
        const tablaProductos = document.querySelector("tbody");
        const nuevaFila = document.createElement("tr");
        nuevaFila.id = `producto-${producto.id}`;
        nuevaFila.innerHTML = `
            
            <td>${producto.nombre}</td>
            <td>${producto.descripcion}</td>
            <td>${producto.categoria.nombreCategoria}</td>
            <td>${producto.cantidad}</td>
            <td>${'$' +producto.precio}</td>
            <td><img src="/imagenes-productos/${producto.imagen}" alt="Imagen del producto" style="width: 60px; height: 60px;"></td>
            <td><a class="btn btn-warning" href="/Productos/Editar/${producto.id}">Editar</a></td>
            
            <td>
                <button class="btn btn-secondary" 
                        type="button" 
                        onclick="desactivarProducto(${producto.id})">
                    Desactivar
                </button>
                
                </td>
            
            
            
            <td><button class="btn btn-danger btn-eliminar" 
                        type="button" 
                        onclick="eliminarProducto(${producto.id})">
                    Eliminar
                </button>
                
                </td>
        `;
 
        tablaProductos.appendChild(nuevaFila);

       //Alerta dew confirmacion
        Swal.fire({
            icon: 'success',
            title: 'Producto agregado',
            showConfirmButton: false,
            timer: 620
        });
    })
    .catch(error => {
        console.error('Error:', error);
        });
}

function eliminarProducto(idProducto) {
	
    //Le mandda la solictud al servidor
    fetch(`/Productos/Eliminar/${idProducto}`, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            
            //Elimina las filas de la vista
            const fila = document.getElementById(`producto-${idProducto}`);
           if (fila) {
                fila.remove();
                
            }
         
        } else {
            throw new Error("No se pudo eliminar el producto");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        Swal.fire(
            "Error",
            "Ocurrió un error al intentar eliminar el producto.",
            "error"
        );
    });
}



function desactivarProducto(idProducto) {
    console.log("ID del producto a eliminar:", idProducto);

    
    Swal.fire({
        title: '¿Estás seguro?',
        text: 'El producto desaparecera para los compradores.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, desactivar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
           
            fetch(`/Productos/DesactivarProducto/${idProducto}`, {
                method: "GET"
            })
            .then(response => {
                if (response.ok) {
                    // Elimina las filas de la vista
                    const fila = document.getElementById(`producto-${idProducto}`);
                    if (fila) {
                        fila.remove();
                    }
                  
                } else {
                    throw new Error("No se pudo desactivar el producto");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                Swal.fire(
                    "Error",
                    "Ocurrió un error al intentar desactivar el producto.",
                    "error"
                );
            });
        }
    });
}


//Activar producto
function activarProducto(idProducto) {
	
	fetch(`/Productos/activarProducto/${idProducto}`, {
    method: 'GET',
})
    .then(response => {
        if (response.ok) {
            
            //Elimina las filas de la vista
            const fila = document.getElementById(`producto-${idProducto}`);
           if (fila) {
                fila.remove();
                
            }
         
        } else {
            throw new Error("No se pudo activar  el producto");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        Swal.fire(
            "Error",
            "Ocurrió un error al intentar activar el producto.",
            "error"
        );
    });
}

//Este metodo se encarga de listar filtrandoi por categorias
document.getElementById("categoria").addEventListener("change", function () {
	
	
	const estadoProducto = document.getElementById("estadoProducto").value;
	
    const categoriaId = this.value;
    fetch(`/Productos/BuscarCategoria?categoria=${categoriaId}&estadoProducto=${estadoProducto}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => response.json())
    .then(data => {
	
	    if(estadoProducto=="1"){
		  actualizarTablaActivos(data.content); 
	    }else{
	     actualizarTablaNoActivos(data.content); 	
	}
       
        
    })
    .catch(error => console.error("Error al buscar productos:", error));
});





//Buscador de productos pr nombre
document.getElementById('searchForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Evita que el formulario se envíe de forma tradicional
   
    const nombreProducto = document.getElementById('nombreProducto').value;
    
    
    const estadoproducto = document.getElementById('estadoProductoBuscador').value;
  
    fetch('/Productos/BuscarProductoAjax', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: `nombreProducto=${encodeURIComponent(nombreProducto)}&estadoProducto=${encodeURIComponent(estadoproducto)}` 
})
    .then(response => response.json()) 
    .then(data => {
	    
        actualizarTablaActivos(data.productos); 
    })
    .catch(error => {
        console.error('Error:', error);
    });
});

