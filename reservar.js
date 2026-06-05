document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formReserva');
    const inputFecha = document.getElementById('fecha');
    const feedback = document.getElementById('feedbackReserva');

    // 1. Bloqueo visual de fechas pasadas
    if (inputFecha) {
        const hoy = new Date().toISOString().split('T')[0];
        inputFecha.setAttribute('min', hoy);
    }

    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault(); 

        // 2. Captura segura de elementos
        const Nombre = document.getElementById('nombreCliente');
        const Correo = document.getElementById('correoCliente');
        const Telef  = document.querySelector('input[name="telefonoCliente"]');
        const fecha  = document.getElementById('fecha');
        const Hora   = document.getElementById('hora'); 
        const Perso  = document.querySelector('input[name="personas"]');

        // 3. Verificación de IDs (Si falta alguno, avisamos por consola)
        if (!Nombre || !Correo || !Telef || !fecha || !Hora || !Perso) {
            console.error("Faltan IDs en el HTML. Revisa los nombres de los campos.");
            return;
        }

        // 4. Validaciones rápidas de Frontend con mensajes nativos
        if (!Nombre.value.trim()) {
            return mostrarNotificacion("Dinos tu nombre: Necesitamos saber a quién esperar.", "warning");
        }
        if (!Hora.value) {
            return mostrarNotificacion("Selecciona hora: Debes elegir un turno de comida o cena.", "warning");
        }

        const datos = {
            nombreCliente: Nombre.value,
            correoCliente: Correo.value,
            telefonoCliente: Telef.value,
            fecha: fecha.value,
            hora: Hora.value, 
            personas: parseInt(Perso.value)
        };

        // Indicador visual de carga
        mostrarNotificacion("Comprobando disponibilidad...", "info");

        try {
            const respuesta = await fetch('/api/public', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(datos)
            });

            if (respuesta.ok) {
                const data = await respuesta.json(); 
                const tokenSeguridad = data.reserva.token;

                // MENSAJE DE ÉXITO NATIVO
                mostrarNotificacion(`¡Perfecto ${Nombre.value}! Tu reserva se ha confirmado. Redirigiendo...`, "success");

                // Redirección tras un pequeño delay para que vean el mensaje de éxito
                setTimeout(() => {
                    window.location.href = `/confirmar?token=${tokenSeguridad}`; 
                }, 2000);
                
            } else {
                // CAPTURAMOS LOS MENSAJES ESPECÍFICOS DEL SERVICIO
                const errorMsg = await respuesta.text();
                
                // Si el mensaje contiene palabras clave, podemos matizar el estilo
                let tipo = "error";
                if (errorMsg.includes("Horario") || errorMsg.includes("pasadas")) {
                    tipo = "warning";
                }

                mostrarNotificacion(errorMsg, tipo);
            }
        } catch (error) {
            mostrarNotificacion("Error de red: No hemos podido conectar con el restaurante.", "error");
        }
    });
});

/**
 * Función para mostrar notificaciones sin usar librerías externas
 * @param {string} mensaje - El texto a mostrar
 * @param {string} tipo - 'success', 'error', 'warning', 'info'
 */
function mostrarNotificacion(mensaje, tipo) {
    const box = document.getElementById('feedbackReserva');
    if (!box) {
        alert(mensaje); // Fallback si no existe el div en el HTML
        return;
    }

    box.textContent = mensaje;
    box.style.display = 'block';
    box.style.padding = '15px';
    box.style.marginBottom = '20px';
    box.style.borderRadius = '5px';
    box.style.textAlign = 'center';
    box.style.fontWeight = 'bold';

    // Colores según el tipo (Estilo nativo profesional)
    switch (tipo) {
        case 'success':
            box.style.backgroundColor = "#d4edda";
            box.style.color = "#155724";
            box.style.border = "1px solid #c3e6cb";
            break;
        case 'error':
            box.style.backgroundColor = "#f8d7da";
            box.style.color = "#721c24";
            box.style.border = "1px solid #f5c6cb";
            break;
        case 'warning':
            box.style.backgroundColor = "#fff3cd";
            box.style.color = "#856404";
            box.style.border = "1px solid #ffeeba";
            break;
        case 'info':
            box.style.backgroundColor = "#d1ecf1";
            box.style.color = "#0c5460";
            box.style.border = "1px solid #bee5eb";
            break;
    }
}

