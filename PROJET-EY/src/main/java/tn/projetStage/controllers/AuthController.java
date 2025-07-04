package tn.projetStage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.projetStage.configuration.JwtUtil;
import tn.projetStage.dto.AuthRequest;
import tn.projetStage.entities.User;
import tn.projetStage.repositories.UserRepository;
import tn.projetStage.services.CustomUserDetailsService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService uds;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Authentification
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCin(), request.getPassword())
        );

        // Chargement des détails utilisateur
        UserDetails userDetails = uds.loadUserByUsername(String.valueOf(request.getCin()));
        String token = jwtUtil.generateToken(userDetails.getUsername());

        // Chargement de l'entité User depuis la BDD
        User user = userRepository.findByCin(request.getCin());

        // Construction de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user); // PAS DE mot de passe si possible !

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }
    @PostMapping("/upload")
    public ResponseEntity<User> uploadUserWithImage(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}