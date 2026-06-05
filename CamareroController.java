package com.daw.proyecto_v2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.daw.proyecto_v2.Services.ReservaService;

@Controller
@RequestMapping("/camarero")
public class CamareroController {


    @GetMapping("")
    public String index() {
        return "panel-camarero"; 
    }

    @GetMapping("/reservas")
    public String verReservas() {
        return "camarero-reservas"; 
    }
}

