document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token_jwt');
    
    if (!token) {
        window.location.href = "/login";
        return;
    }

    const ruta = window.location.pathname;

    // Al cargar Reservas, por defecto mandamos null para que salgan TODAS
    if (ruta.includes("/admin/reservas")) {
        cargarReservas(token, null);
    } else if (ruta.includes("/admin/usuarios") && !ruta.includes("/nuevo")) {
        cargarUsuarios(token);
    } else if (ruta.includes("/admin/usuarios/nuevo")) {
        configurarFormularioRegistro(token);
    }
});

// --- FUNCIÓN 1: GESTIÓN DE RESERVAS (Actualizada con filtro) ---
async function cargarReservas(token, fecha) {
    // Construimos la URL: si hay fecha añadimos el parámetro, si no, va limpia
    const url = fecha ? `/api/admin/reservas?fecha=${fecha}` : '/api/admin/reservas';

    const res = await fetch(url, {
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (res.ok) {
        const reservas = await res.json();
        const tabla = document.getElementById('tablaReservas');
        if (!tabla) return;

        if (reservas.length === 0) {
            tabla.innerHTML = `<tr><td colspan="10" style="text-align:center">No hay reservas encontradas</td></tr>`;
            return;
        }

        tabla.innerHTML = reservas.map(r => `
            <tr>
                <td>${r.idReserva}</td>
                <td><strong>${r.fecha}</strong></td> <!-- Nueva columna de fecha -->
                <td>${r.nombreCliente}</td>
                <td>${r.correoCliente}</td>
                <td>${r.telefonoCliente}</td>
                <td>${r.hora}</td>
                <td>${r.personas}</td>
                <td>${r.mesa ? r.mesa.idMesa : 'Sin asignar'}</td>
                <td><span class="estado-${r.estado.toLowerCase()}">${r.estado}</span></td>
                <td>
                    <button onclick="cancelarReserva(${r.idReserva})" class="btn-cancelar">Cancelar</button>
                    <button onclick="eliminarReserva(${r.idReserva})" class="btn-eliminar" style="color:red">Borrar</button>
                </td>
            </tr>
        `).join('');
    }
}

// --- FUNCIONES DE FILTRADO PARA EL HTML ---
window.actualizarTablaPorFecha = function() {
    const token = localStorage.getItem('token_jwt');
    const fecha = document.getElementById('fechaFiltro').value;
    cargarReservas(token, fecha || null);
};

window.verTodas = function() {
    const token = localStorage.getItem('token_jwt');
    document.getElementById('fechaFiltro').value = ""; // Limpia el input date
    cargarReservas(token, null);
};

// --- FUNCIÓN 2: GESTIÓN DE USUARIOS ---
async function cargarUsuarios(token) {
    const res = await fetch('/api/admin/usuarios', {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (res.ok) {
        const usuarios = await res.json();
        const tabla = document.getElementById('tablaUsuarios');
        if (!tabla) return;

        tabla.innerHTML = usuarios.map(u => `
            <tr>
                <td>${u.idUsuario}</td>
                <td>${u.nombre}</td>
                <td>${u.correo}</td>
                <td><strong>${u.rol}</strong></td>
                <td>
                    <select id="select-rol-${u.idUsuario}">
                        <option value="ADMIN" ${u.rol === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                        <option value="CAMARERO" ${u.rol === 'CAMARERO' ? 'selected' : ''}>CAMARERO</option>
                    </select>
                    <button onclick="cambiarRol(${u.idUsuario})">Cambiar Rol</button>
                    <button onclick="eliminarUsuario(${u.idUsuario})" style="color:red">Eliminar</button>
                </td>
            </tr>
        `).join('');
    }
}

// --- FUNCIÓN 3: REGISTRO DE NUEVO PERSONAL ---
function configurarFormularioRegistro(token) {
    const form = document.getElementById('formNuevoUsuario');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const nuevoU = {
            nombre: document.getElementById('nombre').value,
            correo: document.getElementById('correo').value,
            contrasena: document.getElementById('contrasena').value,
            rol: document.getElementById('rol').value
        };

        const res = await fetch('/api/admin/usuarios', {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` 
            },
            body: JSON.stringify(nuevoU)
        });

        if (res.ok) {
            alert("Empleado creado con éxito");
            window.location.href = "/admin/usuarios";
        } else {
            const error = await res.text();
            alert("Error: " + error);
        }
    });
}

// --- ACCIONES GLOBALES ---

window.cancelarReserva = async function(id) {
    const token = localStorage.getItem('token_jwt');
    if (!confirm("¿Marcar reserva como cancelada?")) return;
    
    const res = await fetch(`/api/admin/reservas/${id}/cancelar`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (res.ok) location.reload();
};

window.eliminarReserva = async function(id) {
    const token = localStorage.getItem('token_jwt');
    if (!confirm("¿BORRAR definitivamente de la base de datos?")) return;
    
    const res = await fetch(`/api/admin/reservas/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (res.ok) location.reload();
};

window.eliminarUsuario = async function(id) {
    const token = localStorage.getItem('token_jwt');
    if (!confirm("¿Eliminar este empleado?")) return;

    const res = await fetch(`/api/admin/usuarios/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (res.ok) location.reload();
};

window.cambiarRol = async function(id) {
    const token = localStorage.getItem('token_jwt');
    const nuevoRol = document.getElementById(`select-rol-${id}`).value;

    const res = await fetch(`/api/admin/usuarios/${id}/rol/${nuevoRol}`, {
        method: 'PATCH',
        headers: { 'Authorization': `Bearer ${token}` }
    });
    
    if (res.ok) {
        alert("Rol actualizado");
        location.reload();
    } else {
        alert("Error al actualizar: " + res.status);
    }
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

