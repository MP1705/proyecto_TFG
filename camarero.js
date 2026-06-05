document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token_jwt');
    if (!token) {
        window.location.href = "/login";
        return;
    }
    // Al cargar, pasamos null para que traiga todas
    cargarReservasCamarero(token, null);

    setInterval(() => {
        const tokenActual = localStorage.getItem('token_jwt');
        // Leemos qué fecha hay en el input en ese momento para refrescar lo correcto
        const fechaSeleccionada = document.getElementById('fechaFiltro').value || null;
        
        console.log("Refrescando datos automáticamente...");
        cargarReservasCamarero(tokenActual, fechaSeleccionada);
    }, 30000);
});


async function cargarReservasCamarero(token, fecha) {
    // CONSTRUIMOS LA URL DINÁMICA
    const url = fecha ? `/api/camarero/reservas?fecha=${fecha}` : '/api/camarero/reservas';

    const res = await fetch(url, {
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (res.ok) {
        const reservas = await res.json();
        const tabla = document.getElementById('tablaReservasCamarero');
        
        if (!tabla) return;
        

        tabla.innerHTML = reservas.map(r => `
            <tr>
                <td>${r.fecha}</td>
                <td>${r.mesa ? r.mesa.idMesa : 'Sin asignar'}</td>
                <td>${r.nombreCliente}</td>
                <td>${r.hora}</td>
                <td>${r.personas}</td>
                <td><strong>${r.estado}</strong></td>
            </tr>
        `).join('');
    } else {
        console.error("Error al cargar reservas:", res.status);
    }
}

window.actualizarTablaPorFecha = function() {
    const token = localStorage.getItem('token_jwt');
    const fechaSeleccionada = document.getElementById('fechaFiltro').value;
    // Ahora sí enviamos la fecha a la función
    cargarReservasCamarero(token, fechaSeleccionada || null);
};

window.verTodas = function() {
    const token = localStorage.getItem('token_jwt');
    document.getElementById('fechaFiltro').value = ""; 
    cargarReservasCamarero(token, null);
};

function cerrarSesion() {
    // 1. Borramos el token para las llamadas a la API
    localStorage.removeItem('token_jwt');
    
    // 2. Borramos la cookie para la navegación entre páginas
    // Importante poner path=/ para que coincida con la que creamos al login
    document.cookie = "token_jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    
    // 3. Vamos al endpoint de logout de Spring para limpiar la sesión del servidor
    window.location.href = "/logout";
}
