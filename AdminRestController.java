package com.daw.proyecto_v2.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.proyecto_v2.Services.ReservaService;
import com.daw.proyecto_v2.Services.UsuarioService;
import com.daw.proyecto_v2.entity.Reserva;
import com.daw.proyecto_v2.entity.Rol;
import com.daw.proyecto_v2.entity.Usuario;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UsuarioService usuarioService;
    private final ReservaService reservaService;

    public AdminRestController(UsuarioService usuarioService, ReservaService reservaService) {
        this.reservaService = reservaService;
        this.usuarioService = usuarioService;
    }


    // Listar todos los usuarios (Camareros/Admins)
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    // Crear un nuevo usuario (Recibe JSON)
    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody Map<String, String> p) {
        try {
            Usuario nuevo = usuarioService.crearUsuario(
                p.get("correo"),
                p.get("nombre"),
                p.get("contrasena"),
                Rol.valueOf(p.get("rol"))
            );
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear usuario: " + e.getMessage());
        }
    }

    // Cambiar el rol de un usuario
    @PatchMapping("/usuarios/{id}/rol/{nuevoRol}")
    public ResponseEntity<?> cambiarRol(@PathVariable Integer id, @PathVariable Rol nuevoRol) {
        try {
            usuarioService.actualizarRol(id, nuevoRol);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar usuario
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    
    // Listar todas las reservas (Para la tabla del admin)
    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> listarTodasLasReservas(
        @RequestParam(required = false) 
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
            List<Reserva> reservas = reservaService.obtenerReservas(fecha);
            return ResponseEntity.ok(reservas);
    }

    // Cancelar reserva (Lógica de negocio: marcar como cancelada)
    @PostMapping("/reservas/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Integer id) {
        try {
            reservaService.cancelarReservaAdmin(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Eliminar reserva de la BD (Borrado físico)
    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }
}