package com.daw.proyecto_v2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.proyecto_v2.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombre(String nombre);
    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByResetToken(String resetToken);
}