package com.daw.proyecto_v2.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.proyecto_v2.entity.Rol;
import com.daw.proyecto_v2.entity.Usuario;
import com.daw.proyecto_v2.repositories.UsuarioRepository;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // Crear empleado (admin crea camareros)
    public Usuario crearUsuario(String correo, String nombre, String password, Rol rol) {

        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setNombre(nombre);
        usuario.setContrasena(passwordEncoder.encode(password));
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    // Cambiar rol
    public void actualizarRol(Integer id, Rol nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Listar empleados
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

     @Transactional
    public void solicitarRecuperacion(String correo) {
    // 1. Buscamos al usuario (el repositorio devuelve Optional)
    Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

    // 2. Si el usuario existe, ejecutamos la lógica
    usuarioOpt.ifPresent(usuario -> {
        // Generar token único
        String token = UUID.randomUUID().toString();
        
        // Guardar token y expiración (15 minutos)
        usuario.setResetToken(token);
        usuario.setTokenExpiration(LocalDateTime.now().plusMinutes(15));
        
        // Persistir cambios
        usuarioRepository.save(usuario);

        // Enviar el correo usando el método que creamos antes
        emailService.enviarCorreoRecuperacion(correo, token);
    });
}

@Transactional
public boolean cambiarPasswordConToken(String token, String nuevaPassword) {
    // 1. Buscar usuario por el token
    Optional<Usuario> usuarioOpt = usuarioRepository.findByResetToken(token);

    if (usuarioOpt.isPresent()) {
        Usuario usuario = usuarioOpt.get();

        // 2. Verificar si el token ha expirado
        if (usuario.getTokenExpiration().isAfter(LocalDateTime.now())) {
            
            // 3. Encriptar y guardar la nueva contraseña
            usuario.setContrasena(passwordEncoder.encode(nuevaPassword));

            // 4. IMPORTANTE: Limpiar el token para que sea de UN SOLO USO
            usuario.setResetToken(null);
            usuario.setTokenExpiration(null);

            usuarioRepository.save(usuario);
            return true;
        }
    }
    return false; // Token inválido o expirado
}


}



