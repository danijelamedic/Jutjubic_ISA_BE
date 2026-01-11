package jutjubic.isa.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jutjubic.isa.backend.dto.JwtResponseDTO;
import jutjubic.isa.backend.dto.LoginRequestDTO;
import jutjubic.isa.backend.dto.RegistrationRequestDTO;
import jutjubic.isa.backend.dto.ResponseMessageDTO;
import jutjubic.isa.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessageDTO> register(@RequestBody RegistrationRequestDTO req) {
        String token = authService.register(req);
        return ResponseEntity.ok(new ResponseMessageDTO("Registrovan. Token: " + token));
    }

    @GetMapping("/activate")
    public ResponseEntity<ResponseMessageDTO> activate(@RequestParam("token") String token) {
        authService.activate(token);
        return ResponseEntity.ok(new ResponseMessageDTO("Nalog je uspešno aktiviran."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req, HttpServletRequest httpReq) {
        String ip = httpReq.getRemoteAddr();
        String token = authService.login(req.getEmail(), req.getPassword(), ip);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }


}
