package tn.projetStage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.projetStage.configuration.JwtUtil;
import tn.projetStage.dto.AuthRequest;
import tn.projetStage.dto.ChangePasswordRequest;
import tn.projetStage.entities.User;
import tn.projetStage.repositories.UserRepository;
import tn.projetStage.services.CustomUserDetailsService;
import tn.projetStage.services.EmailService;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private  CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService uds;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private  PasswordEncoder passwordEncoder;

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
    }@GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        // On ne retourne pas les mots de passe pour des raisons de sécurité
        return ResponseEntity.ok(
                userRepository.findAll().stream().map(user -> {
                    user.setPassword(null); // Supprimer le mot de passe avant envoi
                    return user;
                }).toList()
        );
    }
    @PutMapping("/accept/{id}")
    public ResponseEntity<?> acceptUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé.");
        }

        try {
            String tempPassword = generateRandomPassword();
            String encodedPassword = passwordEncoder.encode(tempPassword);

            user.setPassword(encodedPassword);
            user.setStatus(true); // ✅ booléen accepté
            userRepository.save(user);

            emailService.sendAcceptedEmail(
                    user.getEmail(),
                    user.getNom(),
                    user.getCin(),
                    tempPassword,
                    String.valueOf(user.getRole())
            );

            return ResponseEntity.ok("Utilisateur accepté et mail envoyé.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de l'envoi de l'email d'acceptation : " + e.getMessage());
        }
    }



    public String generateRandomPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectUser(@PathVariable Long id, @RequestParam(required = false) String reason) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé.");
        }

        try {
            emailService.sendRejectionEmail(
                    user.getEmail(),
                    user.getNom(),
                    reason != null ? reason : "Non spécifiée"
            );

            user.setStatus(false); // ❌ booléen refusé
            userRepository.save(user);

            return ResponseEntity.ok("Utilisateur refusé et mail envoyé.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de l'envoi de l'email de refus : " + e.getMessage());
        }
    }



    @PostMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        boolean success = customUserDetailsService.changePassword(id, request);
        if (success) {
            return ResponseEntity.ok("Mot de passe changé avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Ancien mot de passe incorrect ou utilisateur introuvable.");
        }
    }

    @PutMapping("/update-profile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody User updatedData) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé.");
        }

        // On ne touche pas au mot de passe ni au rôle
        existingUser.setCin(updatedData.getCin());
        existingUser.setNom(updatedData.getNom());
        existingUser.setEmail(updatedData.getEmail());
        existingUser.setImage(updatedData.getImage());

        userRepository.save(existingUser);

        // Par sécurité, on retire le mot de passe de la réponse
        existingUser.setPassword(null);

        return ResponseEntity.ok(existingUser);
    }

}