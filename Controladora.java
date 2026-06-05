package com.daw.proyecto_v2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controladora {

          @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/carta")
    public String carta() {
        return "carta";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String mostrarPaginaOlvidarPassword() {
        return "forgot-password"; 
    }

   
    @GetMapping("/reset-password")
    public String mostrarPaginaResetPassword() {
        return "reset-password"; 
    }
}
