document.addEventListener('DOMContentLoaded', async () => {
    // 1. Extraemos el ID de la URL (ej: /confirmar?id=125)
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    // Si no hay ID en la URL, redirigimos al inicio por seguridad
    if (!token) {
        window.location.href = '/';
        return;
    }

    try {
        // 2. Hacemos el fetch GET a la nueva ruta de tu controladora
        const respuesta = await fetch(`/api/public/${token}`);

        if (respuesta.ok) {
            const reserva = await respuesta.json();

            // 3. Pintamos los datos en el HTML usando los IDs de los <span>
            
            document.getElementById('nombreCliente').textContent = reserva.nombreCliente;
            document.getElementById('correoCliente').textContent = reserva.correoCliente;
            document.getElementById('telefonoCliente').textContent = reserva.telefonoCliente;
            document.getElementById('fecha').textContent = reserva.fecha;
            document.getElementById('hora').textContent = reserva.hora;
            document.getElementById('personas').textContent = reserva.personas;
            
           

        } else {
            console.error("No se encontró la reserva");
            alert("Error: La reserva no existe.");
        }
    } catch (error) {
        console.error("Error de conexión al obtener la reserva", error);
    }
});
