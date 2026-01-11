package jutjubic.isa.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jutjubic.isa.backend.dto.JwtResponse;
import jutjubic.isa.backend.dto.LoginRequest;
import jutjubic.isa.backend.dto.RegistrationRequest;
import jutjubic.isa.backend.dto.ResponseMessage;
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
    public ResponseEntity<ResponseMessage> register(@RequestBody RegistrationRequest req) {
        String token = authService.register(req);
        return ResponseEntity.ok(new ResponseMessage("Registrovan. Token: " + token));
    }

    @GetMapping("/activate")
    public ResponseEntity<ResponseMessage> activate(@RequestParam("token") String token) {
        authService.activate(token);
        return ResponseEntity.ok(new ResponseMessage("Nalog je uspešno aktiviran."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletRequest httpReq) {
        String ip = httpReq.getRemoteAddr();
        String token = authService.login(req.getEmail(), req.getPassword(), ip);
        return ResponseEntity.ok(new JwtResponse(token));
    }


}
