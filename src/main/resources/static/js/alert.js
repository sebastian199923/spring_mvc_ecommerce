
if (mensajeAlerta) {
    let alertTitle = '';
    
    
    
    switch (tipoAlerta) {
        case 'success':
              if (mensajeAlerta.includes('con exito')) {
                  alertTitle = '¡Correo enviado!';
              } else if (mensajeAlerta.includes('usuario ha sido registrado.')) {
                alertTitle = '¡Usuario registrado!';
              }else if (mensajeAlerta.includes('actualizada')) {
                alertTitle = '¡Contraseña actualizada!';
              }else if (mensajeAlerta.includes('sido actualizado')) {
                alertTitle = '¡ha sido actualizado!';
              }else if (mensajeAlerta.includes('ha sido agregado')) {
                alertTitle = '¡El producto ha sido agregado!';
              }else if (mensajeAlerta.includes('categoria ha sido agregada')) {
                alertTitle = '¡La categoria ha sido agregada!';
              }
              
              
              break;
        case 'error':
              if (mensajeAlerta.includes('invalidas')) {
                alertTitle = '¡Credenciales invalidas!';
              } else if (mensajeAlerta.includes('stock')) {
                alertTitle = '¡No hay más productos!';
              } else if (mensajeAlerta.includes('Codigo erroneo')) {
                alertTitle = '¡Codigo erroneo!';
              }  else if (mensajeAlerta.includes('correo no encontrado')) {
                alertTitle = '¡Correo no encontrado!';
              } else if (mensajeAlerta.includes('Se ha producido un error')) {
                alertTitle = 'Se ha producido un error';
                
              }else if (mensajeAlerta.includes('correo ya ha sido registrado en el sistema')) {
                alertTitle = 'Correo ya registrado en el sistema';
                
              }else if (mensajeAlerta.includes('cedula ya ha sido registrada en el sistema')) {
                alertTitle = 'Cedula ya registrada en el sistema';
              }else if (mensajeAlerta.includes('contraseña no coincide')) {
                alertTitle = 'La contraseña no coincide';
              }
              
              
              
              
              else {
                alertTitle = '¡error!';
              }
              break;
         
        default:
            alertTitle = '¡Atención!';
    }

    // Mostrar alerta con SweetAlert
    Swal.fire({
        title: alertTitle,
        text: mensajeAlerta,
        icon: tipoAlerta,
        showConfirmButton: false,
        timer: 1400,
        timerProgressBar: true
    });
}


const contrasena = document.getElementById('contrasena');
const contrasena1 = document.getElementById('contrasena1');
const errorMessage = document.getElementById('error-message');
const numeroCaracteres = document.getElementById('numeroCaracteres');
const submitBtn = document.getElementById('btn-cambiocontraseña');
// Agregar un "event listener" para cada uno de los campos
contrasena.addEventListener('input', compararContrasenas);
contrasena1.addEventListener('input', compararContrasenas);


// Función que compara las contraseñas
function compararContrasenas() {
// Verificar si las contraseñas coinciden
const longitudContrasena = contrasena.value.length;


if(longitudContrasena>=8){
	numeroCaracteres.style.display = 'none';
	
	if(contrasena.value === contrasena1.value && contrasena.value !== "" ){
		 errorMessage.style.display = 'none';
         submitBtn.style.display = 'block';
	}else{
		errorMessage.style.display = 'block';
	}
	
	
   
}else{
	numeroCaracteres.style.display = 'block';
}

        }









$(document).ready(function() {
    // Asegurarse de que el modal se inicialice correctamente
    $('#editarModal').modal({
        show: false
    });
});




$(document).ready(function() {
    // Asegurarse de que el modal se inicialice correctamente
    $('##agregarProductoModal').modal({
        show: false
    });
});






