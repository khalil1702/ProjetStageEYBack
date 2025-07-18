package tn.projetStage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.projetStage.dto.ChangePasswordRequest;
import tn.projetStage.entities.User;
import tn.projetStage.repositories.UserRepository;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByCin(username);
        if (user == null) {
            user = userRepository.findByEmail(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable avec le CIN ou email : " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getCin(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public boolean changePassword(Long userId, ChangePasswordRequest request) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false; // Utilisateur introuvable
        }

        User user = optionalUser.get();

        // Vérification de l'ancien mot de passe
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return false; // Ancien mot de passe incorrect
        }

        // Mise à jour du nouveau mot de passe
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }






}
