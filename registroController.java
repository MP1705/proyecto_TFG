package com.daw.proyecto_v2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.proyecto_v2.entity.Rol;
import com.daw.proyecto_v2.entity.Usuario;
import com.daw.proyecto_v2.repositories.UsuarioRepository;

@Controller
public class registroController {

       @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro"; 
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String correo, 
                                   @RequestParam String nombre, 
                                   @RequestParam String contrasena) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setNombre(nombre);
        usuario.setRol(Rol.ADMIN); 
        
       
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        
        usuarioRepository.save(usuario);
        return "redirect:/login"; 
    }
}
