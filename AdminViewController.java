package com.daw.proyecto_v2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.proyecto_v2.Services.ReservaService;
import com.daw.proyecto_v2.Services.UsuarioService;
import com.daw.proyecto_v2.entity.Rol;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping("")
    public String index() { 
        return "admin-panel";
     }

    @GetMapping("/reservas")
    public String verReservas() {
         return "reservas"; 
        } 

    @GetMapping("/usuarios")
    public String verUsuarios() { 
        return "listar-roles"; 
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario() { 
        return "crear-camarero";
     }

    
}
