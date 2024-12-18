// Función para generar la plantilla de un producto
function generarPlantillaProducto(producto) {
    return `
        <div class="product-item">
            <figure>
                <a href="/Public/DetalleProducto/${producto.id}" title="${producto.nombre}">
                    <img src="/imagenes-productos/${producto.imagen}" id="imagenProducto" alt="${producto.nombre}" class="tab-image">
                </a>
            </figure>
            <div class="d-flex flex-column text-center">
                <h3 class="fs-6 fw-normal">${producto.nombre}</h3>
                <div class="d-flex justify-content-center align-items-center gap-2">
                    <span class="text-dark fw-semibold">${producto.precio}</span>
                </div>
                <form  id="formAgregarCarro" action="/Public/AgregarCarroAjax" method="post">
                    <div class="button-area p-3 pt-0">
                        <div class="row g-1 mt-2">
                            <div class="col-3">
                                <input type="number" name="cantidad" class="form-control border-dark-subtle input-number quantity" value="1" max="${producto.cantidad}" min="1">
                                <input type="hidden" name="id" value="${producto.id}">
                                <input type="hidden" name="pantalla" value="1">
                            </div>
                            <div class="col-7">
                                <button type="submit" class="btn btn-primary rounded-1 p-2 fs-7 btn-cart"  onclick="agregarCarro(event)">
                                    <svg width="18" height="18"><use xlink:href="#cart"></use></svg>
                                    Añadir al carro
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>`;
}



function eliminarProducto(idProducto) {
    
        fetch(`/Public/EliminarProductoCarro/${idProducto}`)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    console.log(`Producto eliminado correctamente: ${idProducto}`);
                    
                    console.log(data.total)
                    // Eliminar la fila correspondiente
                    const fila = document.getElementById(`producto-${idProducto}`);
                    if (fila) {
                        fila.remove();
                    } else {
                        console.error(`No se encontró la fila con id: producto-${idProducto}`);
                    }

                    // Actualizar el total del carrito
                    const totalCarro = document.getElementById("total-carro");
                    if (totalCarro) {
                        totalCarro.textContent = `Total: ${data.total.toFixed(2)}`;
                    }

                    console.log("Producto eliminado y total actualizado");
                } else {
                    alert("Error al eliminar el producto: " + data.message);
                }
            })
            .catch(error => {
                console.error("Error en la solicitud:", error);
                alert("No se pudo eliminar el producto.");
            });
   
}










function agregarCarro(event) {
    event.preventDefault(); // Evita el envío del formulario

    console.log("ENTRA A JAVASCRIPT");

    // Obtiene el formulario
    const form = event.target.closest('form');  // Encuentra el formulario más cercano al botón que fue clickeado

    // Accede a los valores de los campos del formulario
    const cantidad = form.querySelector('[name="cantidad"]').value;
    const id = form.querySelector('[name="id"]').value;
    const pantalla = form.querySelector('[name="pantalla"]').value;

    // Imprimir los valores en la consola
    console.log("Cantidad:", cantidad);
    console.log("ID Producto:", id);
    console.log("Pantalla:", pantalla);

    
    const formData = new FormData(form);  // Crear el FormData a partir del formulario

    // Enviar los datos usando AJAX (por ejemplo, con fetch)
    fetch(form.action, {
        method: form.method,
        body: formData
    })
    .then(response => response.json())
    .then(data => {
	  
        const contadorCarrito = document.getElementById('contadorCarrito');
        
        contadorCarrito.textContent = data.cantidadProductos;
        Swal.fire({
                        icon: 'success',
                        title: 'Agregado al carrito',
                        text: data.mensaje, 
                        showConfirmButton: false,
                        timer: 620
                    });
    })
    .catch(error => {
	      if (error.toString().includes("No hay")) {
                 Swal.fire({
                        icon: 'warning',
                        title: 'No hay mas productos en stock',
                        showConfirmButton: false,
                        timer: 750
                    });
        }
        
    });
}







function buscarPorCategoriaAjax() {
    const categoriaId = document.getElementById('categoria').value;

    fetch(`/BuscarCategoriaAjax?categoria=${categoriaId}`)
        .then(response => response.json())
        .then(data => {
            const contenedorProductos = document.getElementById('productos');
            contenedorProductos.innerHTML = '';

            if (data.length === 0) {
                contenedorProductos.innerHTML = '<p>No se encontraron productos en esta categoría.</p>';
            } else {
                data.forEach(producto => {
                    const productoDiv = document.createElement('div');
                    productoDiv.classList.add('col');
                    productoDiv.innerHTML = generarPlantillaProducto(producto);
                    contenedorProductos.appendChild(productoDiv);
                });
            }
        })
        .catch(error => {
            console.error('Error al buscar productos:', error);
        });
}



 function buscarProductoAjax() {
    // Obtener el valor ingresado por el usuario en el campo de búsqueda
    const nombreProducto = document.querySelector('input[name="nombreProducto"]').value;
    console.log(nombreProducto)
    // Realizar la solicitud AJAX al servidor
    fetch(`/Public/BuscadorAjax?nombreProducto=${encodeURIComponent(nombreProducto)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al buscar productos');
            }
            return response.json();
        })
        .then(data => {
            const contenedorProductos = document.getElementById('productos');
            contenedorProductos.innerHTML = ''; // Limpiar productos existentes

            if (data.length === 0) {
                contenedorProductos.innerHTML = '<p>No se encontraron productos.</p>';
            } else {
                // Iterar y crear la plantilla para cada producto
                data.forEach(producto => {
                    const productoDiv = document.createElement('div');
                    productoDiv.classList.add('col');
                    productoDiv.innerHTML =  generarPlantillaProducto(producto);
                    contenedorProductos.appendChild(productoDiv);
                });
            }
        })
        .catch(error => {
            console.error('Error al buscar productos:', error);
        });
}







