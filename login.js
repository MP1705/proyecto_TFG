document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formLogin');

    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // 1. Captura de elementos
        const elCorreo = document.getElementById('correo');
        const elPass = document.getElementById('contrasena');
        
        notificarLogin("Verificando credenciales...", "info");

        if (!elCorreo || !elPass) {
            console.error("No se encuentran los inputs 'correo' o 'contrasena'");
            return;
        }

        const loginRequest = {
            correo: elCorreo.value,
            contrasena: elPass.value
        };

        try {
            // 2. Petición Fetch al API de autenticación
            // Nota: No incluimos headers de CSRF porque lo hemos desactivado en el Backend
            const respuesta = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginRequest)
            });

            if (respuesta.ok) {
                const data = await respuesta.json(); 
                // data contiene: { "token": "...", "rol": "ROLE_ADMIN" }

                // --- PERSISTENCIA ---
                // 1. Guardamos en LocalStorage para futuras peticiones AJAX/Fetch
                localStorage.setItem('token_jwt', data.token);
                
                // 2. Guardamos en Cookie para que Spring Security lo lea al cambiar de página
                // Expira en 1 hora (3600 seg)
                document.cookie = `token_jwt=${data.token}; path=/; max-age=3600; SameSite=Strict`;

                notificarLogin("¡Acceso correcto! Redirigiendo...", "success");

                // --- REDIRECCIÓN ---
                setTimeout(() => {
                    const rol = data.rol ? data.rol.toUpperCase().trim() : "";

                    console.log("Redirigiendo con rol:", rol);

                    if (rol === 'ROLE_ADMIN' || rol === 'ADMIN') {
                        window.location.assign("/admin");
                    } else if (rol === 'ROLE_CAMARERO' || rol === 'CAMARERO') {
                        window.location.assign("/camarero");
                    } else {
                        window.location.assign("/");
                    }
                }, 800);

            } else {
                // Errores de credenciales o de servidor
                if (respuesta.status === 401 || respuesta.status === 403) {
                    notificarLogin("Correo o contraseña incorrectos.", "error");
                } else {
                    notificarLogin("Error en el servidor. Inténtalo de nuevo.", "error");
                }
            }
        } catch (error) {
            console.error("Error en el login:", error);
            notificarLogin("No se pudo conectar con el servidor.", "error");
        }
    });
});

/**
 * Función para mostrar notificaciones de Login en el HTML
 */
function notificarLogin(mensaje, tipo) {
    const box = document.getElementById('feedbackLogin');
    if (!box) return;

    if (!mensaje) {
        box.style.display = 'none';
        return;
    }

    box.textContent = mensaje;
    box.style.display = 'block';
    box.style.padding = '10px';
    box.style.marginBottom = '15px';
    box.style.borderRadius = '5px';
    box.style.textAlign = 'center';
    box.style.fontWeight = 'bold';

    if (tipo === 'success') {
        box.style.backgroundColor = "#d4edda";
        box.style.color = "#155724";
        box.style.border = "1px solid #c3e6cb";
    } else if (tipo === 'error') {
        box.style.backgroundColor = "#f8d7da";
        box.style.color = "#721c24";
        box.style.border = "1px solid #f5c6cb";
    } else {
        box.style.backgroundColor = "#d1ecf1";
        box.style.color = "#0c5460";
        box.style.border = "1px solid #bee5eb";
    }
}



