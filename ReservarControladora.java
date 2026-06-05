package com.daw.proyecto_v2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.proyecto_v2.DTO.crearReservaDTO;
import com.daw.proyecto_v2.Services.EmailService;
import com.daw.proyecto_v2.Services.ReservaService;
import com.daw.proyecto_v2.entity.Reserva;

@Controller
public class ReservarControladora {

    private final ReservaService reservaService;
    private final EmailService emailService; // 1. Inyectamos el servicio de correo

    public ReservarControladora(ReservaService reservaService, EmailService emailService) {
        this.reservaService = reservaService;
        this.emailService = emailService;
    }

    @GetMapping("/reservar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("reserva", new crearReservaDTO());
        return "Reservar";
    }

     @GetMapping("/confirmar")
public String mostrarConfirmacion(@RequestParam String token, Model model) {
    // 1. Buscamos la reserva directamente por el TOKEN único
    // Si el token es correcto, ya es seguro mostrarla.
    Reserva reserva = reservaService.buscarPorToken(token)
            .orElse(null);

    // 2. Si el token no existe en la BD, entonces sí redirigimos al index
    if (reserva == null) {
        return "redirect:/"; 
    }

    model.addAttribute("reserva", reserva);
    return "confirmar"; 
    }
}