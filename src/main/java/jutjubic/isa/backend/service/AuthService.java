package jutjubic.isa.backend.service;

import jutjubic.isa.backend.dto.RegistrationRequest;
import jutjubic.isa.backend.model.ActivationToken;
import jutjubic.isa.backend.model.Role;
import jutjubic.isa.backend.model.User;
import jutjubic.isa.backend.repository.ActivationTokenRepository;
import jutjubic.isa.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ActivationTokenRepository activationTokenRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository,
                       ActivationTokenRepository activationTokenRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.activationTokenRepository = activationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String register(RegistrationRequest req) {
        // required polja
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email je obavezan.");
        }
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new IllegalArgumentException("Korisničko ime je obavezno.");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new IllegalArgumentException("Lozinka je obavezna.");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("Lozinke se ne poklapaju.");
        }

        // validacije
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email je već zauzet.");
        }
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Korisničko ime je već zauzeto.");
        }

        // kreiranje usera
        User user = new User();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());

        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
//        String hash = passwordEncoder.encode(req.getPassword());
//        System.out.println("HASH = " + hash);
//        user.setPasswordHash(hash);

        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setAddress(req.getAddress());
        user.setEnabled(false);
        user.setRole(Role.USER);

        user = userRepository.save(user);

        // activation token
        String tokenValue = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(Duration.ofHours(24));

        ActivationToken token = new ActivationToken();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setExpiresAt(expiresAt);

        activationTokenRepository.save(token);

        // 5) "slanje email-a" za sada: vrati link ili loguj (kasnije pravi mail sender)
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return tokenValue;
    }

    public void activate(String tokenValue) {
        if (tokenValue == null || tokenValue.isBlank()) {
            throw new IllegalArgumentException("Token je obavezan.");
        }

        Optional<ActivationToken> opt = activationTokenRepository.findByToken(tokenValue);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Neispravan token.");
        }

        ActivationToken token = opt.get();

        if (token.getExpiresAt().isBefore(Instant.now())) {
            // brisanje tokena koji je istekao ukoliko se korisnik nije registrovao na vreme
            activationTokenRepository.delete(token);
            throw new IllegalArgumentException("Token je istekao.");
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        // brisanje tokena kad sve zavrsi
        activationTokenRepository.delete(token);
    }
}
