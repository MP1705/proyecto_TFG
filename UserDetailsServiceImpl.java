package com.daw.proyecto_v2.Security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.daw.proyecto_v2.entity.Usuario;
import com.daw.proyecto_v2.repositories.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new UsuarioPrincipal(usuario);
    }
}
