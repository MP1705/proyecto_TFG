package com.daw.proyecto_v2.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

     @Autowired
    private JavaMailSender mailSender;

    @Async
    public void enviarConfirmacion(String destinatario, String nombreCliente, String fecha) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("reservas@BienHecho.com");
        message.setTo(destinatario);
        message.setSubject("Confirmación de Reserva - Tu Restaurante");
       String cuerpoMensaje = "Hola " + nombreCliente + ",\n\n" +
                           "Tu reserva para el día " + fecha + " ha sido confirmada con éxito.\n\n" +
                           "--------------------------------------------------\n" +
                           "IMPORTANTE: Si deseas cancelar tu reserva, por favor " +
                           "llámanos al +34 643 81 76 59\n" + 
                           "--------------------------------------------------\n\n" +
                           "¡Te esperamos!";

    message.setText(cuerpoMensaje);
    
        
        mailSender.send(message);
    }

    @Async
public void enviarCorreoRecuperacion(String destinatario, String token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("soporte@BienHecho.com");
    message.setTo(destinatario);
    message.setSubject("Restablecer Contraseña - BienHecho");

    String url = "http://localhost:8080/reset-password?token=" + token;

    String cuerpoMensaje = "Has solicitado restablecer tu contraseña en BienHecho.\n\n" +
                           "Haz clic en el siguiente enlace para elegir una nueva contraseña:\n" +
                           url + "\n\n" +
                           "Este enlace caducará en 15 minutos.\n" +
                           "Si no has solicitado esto, puedes ignorar este correo.";

    message.setText(cuerpoMensaje);
    mailSender.send(message);
}
}
