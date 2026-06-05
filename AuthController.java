package com.daw.proyecto_v2.RestController;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.daw.proyecto_v2.DTO.AuthResponse;
import com.daw.proyecto_v2.DTO.LoginRequest;
import com.daw.proyecto_v2.Security.JwtService;
import com.daw.proyecto_v2.Services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("Intentando autenticar a: " + request.getCorreo());

            // 1. Autenticación contra la base de datos
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContrasena())
            );

            // 2. Generar el Token JWT
            String token = jwtService.generateToken(authentication);

            // 3. Extraer el ROL del usuario autenticado
            // Spring Security guarda los roles en las "Authorities"
            String rol = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_USER"); // Rol por defecto si no tiene

            // 4. Devolver respuesta profesional con Token y Rol
            return ResponseEntity.ok(new AuthResponse(token, rol));

        } catch (AuthenticationException e) {
            // Si las credenciales fallan, devolvemos un 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Correo o contraseña incorrectos");
        } catch (Exception e) {
            // Error genérico del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno en el servidor");
        }
    }

    @PostMapping("/forgot-password")
public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
    System.out.println("hola");
    String correo = body.get("correo");
    usuarioService.solicitarRecuperacion(correo);
    return ResponseEntity.ok("Si el correo está registrado, recibirás un enlace de recuperación.");
}

@PostMapping("/reset-password")
public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
    String token = body.get("token");
    String nuevaPassword = body.get("nuevaPassword");

    boolean exito = usuarioService.cambiarPasswordConToken(token, nuevaPassword);

    if (exito) {
        return ResponseEntity.ok("Contraseña cambiada con éxito");
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido o expirado");
    }
}


}
