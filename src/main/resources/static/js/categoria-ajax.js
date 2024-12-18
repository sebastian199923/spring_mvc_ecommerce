function actualizarTabla(categorias) {
    const tablaCuerpo = document.querySelector(".table tbody");
    tablaCuerpo.innerHTML = ""; // Limpia el contenido actual de la tabla
    categorias.forEach(categoria => {
	const fila = document.createElement("tr");
       file.id = `categoria-${categoria.id}`;
       fila.innerHTML = `
        
            <td>${categoria.nombreCategoria}</td>
            <td>
                <a class="btn btn-warning" href="/Categorias/Editar/${categoria.id}">Editar</a>
            </td>
            
           <td> <a class="btn btn-danger btn-eliminar" type="button" onclick="eliminarCategoria(${categoria.id})"> Eliminar </a></td>
        `;

        tablaCuerpo.appendChild(fila);
    });
}




//Funcion encargada del registro de categorias
function registarCategoria(event) {
	event.preventDefault();
    const form = document.getElementById('formRegistrarCategoria');
    if (!form.checkValidity()) {
        form.reportValidity(); 
        return;
        }
    const formData = new FormData(form);
    fetch('/Categorias/GuardarCategoria', {
        method: 'POST',
        body: formData,
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error en la solicitud: ${response.statusText} (Código de estado: ${response.status})`);
        }
        return response.json(); 
    })
    
    .then(Categoria => {
        $('#agregarCategoriaModal').modal('hide');
        form.reset();
        const tablaCategoria = document.querySelector("tbody");
        const nuevaFila = document.createElement("tr");
        nuevaFila.id = `categoria-${Categoria.id}`;
        nuevaFila.innerHTML = `
            <td>${Categoria.nombreCategoria}</td>
            <td><a class="btn btn-warning" href="/Categorias/Editar/${Categoria.id}">Editar</a></td>
            <td> <a class="btn btn-danger btn-eliminar" type="button" onclick="eliminarCategoria(${Categoria.id})"> Eliminar </a></td>
        `;
        // Agrega una nueva fila
        tablaCategoria.appendChild(nuevaFila);

       //Alerta dew confirmacion
        Swal.fire({
            icon: 'success',
            title: 'Categoria agregada',
            showConfirmButton: false,
            timer: 700
        });
    })
    .catch(error => {
        console.error('Error:', error);
        });
}







//Buscador de ctegorias por nombre
document.getElementById('buscarCategoria').addEventListener('submit', function(e) {
    e.preventDefault();
  
    const nombreCategoria = document.getElementById('nombreCategoria').value;
    console.log(nombreCategoria)
    fetch('/Categorias/BuscarCategoriaAjax', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: `nombreCategoria=${encodeURIComponent(nombreCategoria)}` 
})
    .then(response => response.json()) 
    .then(data => {
	    
	   
        actualizarTabla(data.categorias); 
    })
    .catch(error => {
        console.error('Error:', error);
    });
});




function eliminarCategoria(idCategoria) {
    fetch(`/Categorias/Eliminar/${idCategoria}`, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
        const fila = document.getElementById(`categoria-${idCategoria}`);
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
            "Ocurrió un error al intentar eliminar la categoria.",
            "error"
        );
    });
}