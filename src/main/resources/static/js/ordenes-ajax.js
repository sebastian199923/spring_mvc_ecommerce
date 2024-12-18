function actualizarTabla(ordenes) {
    const tablaCuerpo = document.querySelector(".table tbody");
    tablaCuerpo.innerHTML = ""; // Limpia el contenido actual de la tabla

    ordenes.forEach(orden => {
        const fila = document.createElement("tr");

        fila.innerHTML = `
            <td>${orden.numero}</td>
            <td>${new Date(orden.fechaCreacion).toLocaleDateString("es-ES")}</td>
            <td>${orden.usuario.nombre} ${orden.usuario.apellido}</td>
            <td>$${orden.total.toLocaleString("es-ES", { minimumFractionDigits: 0, maximumFractionDigits: 0 })}</td>
            <td>
                <a class="btn" style="background-color: #b0e57c; color: #fff;" 
                   href="/User/DetalleCompra/${orden.id}">Detalles</a>
            </td>
        `;
        tablaCuerpo.appendChild(fila);
    });
}




document.getElementById('buscadorOrdenes').addEventListener('submit', function(e) {
    e.preventDefault(); // Evita que el formulario se envíe de forma tradicional
    console.log("ENTRO a buscador ordenes");
    
    const numeroOrden = document.getElementById('numeroOrden').value;
    console.log(numeroOrden);
    
    fetch('/Orden/BuscarOrdenAjax', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `numeroOrden=${encodeURIComponent(numeroOrden)}`
    })
    .then(response => response.json())  // Convierte la respuesta a formato JSON
    .then(ordenes => {
	
    
    
    
    actualizarTabla(ordenes);
})
    .catch(error => {
        console.error('Error:', error);
    });
});

