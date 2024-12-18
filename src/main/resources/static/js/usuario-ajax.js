
//Buscador usuarios por cedula
function buscarUsuario(event) {
    event.preventDefault(); 
  

    const cedulaUsuario = document.getElementById('cedulaUsuario').value;
    

    fetch('/admin/BuscarUsuarioAjax', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `cedulaUsuario=${encodeURIComponent(cedulaUsuario)}`
    })
    .then(response => response.json())
    .then(data => {
        // Actualizar la tabla con los resultados
        const tbody = document.querySelector('table tbody');
        tbody.innerHTML = ''; // Limpia la tabla actual

        console.log(data);
        data.usuarios.forEach(usuario => {
            const row = `
                <tr>
                    <td>${usuario[1]}</td> <!-- nombre -->
                    <td>${usuario[2]}</td> <!-- apellido -->
                    <td>${usuario[3]}</td> <!-- cédula -->
                    <td>${usuario[4]}</td> <!-- correo -->
                    <td>${usuario[5]}</td> <!-- teléfono -->
                    <td>${usuario[6]}</td> <!-- tipo -->
                    <td>${usuario[7]}</td> <!-- número de compras -->
                    <td>${new Intl.NumberFormat('es-CO').format(usuario[8])}</td> <!-- valor -->
                </tr>
            `;
            tbody.insertAdjacentHTML('beforeend', row);
        });
    })
    .catch(error => {
        console.error('Error:', error);
    });
}


//Editar Usuario
document.getElementById('guardarCambios').addEventListener('click', function () {
	event.preventDefault();
    const form = document.getElementById('editarUsuarioForm');

     //Evista que se envien valores en blanco
     if (!form.checkValidity()) {
        form.reportValidity(); 
        return;
    }
  
    // Crear el objeto FormData
    const formData = new FormData(form);

    // Enviar la solicitud AJAX
    fetch('/User/Editar', {
        method: 'POST',
        body: new URLSearchParams(formData),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.text(); // Obtener la respuesta del backend
        } else {
            throw new Error('Error al actualizar el usuario.');
        }
    })
    .then(data => {
        // Ocultar el modal solo si la solicitud fue exitosa
        $('#editarModal').modal('hide');

        // Actualizar los datos en el frontend
        document.getElementById(`nombre`).textContent = formData.get("nombre");
        document.getElementById(`apellido`).textContent = formData.get("apellido"); 
        document.getElementById(`cedula`).textContent = formData.get("cedula"); 
        document.getElementById(`correo`).textContent = formData.get("correo"); 
        document.getElementById(`direccion`).textContent = formData.get("direccion"); 
        document.getElementById(`telefono`).textContent = formData.get("telefono");    
        document.getElementById(`tipo`).textContent = formData.get("tipo");   

        console.log('Datos actualizados en el frontend.');
    })
    .catch(error => {
        console.error('Error:', error);
    });
});





//Valida la contraseña proporcionada
function submitEditarContrasena(event) {
        event.preventDefault(); 
        
        const form = document.querySelector('#formEditarContrasena');
        //Evista que se envien valores en blanco
        if (!form.checkValidity()) {
        form.reportValidity(); 
        return;
        }
        const formData = new FormData(form);
        const url = form.getAttribute('action');
        const inputContrasena = document.querySelector('#comparacontrasena');

        fetch(url, {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
               
                window.location.href = data.redirectUrl;
            } else if (data.status === 'error') {
	
        inputContrasena.value = '';
        Swal.fire({
            icon: 'error',
            title: 'Contraseña incorrecta',
            showConfirmButton: false,
            timer: 620
        });
              
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

