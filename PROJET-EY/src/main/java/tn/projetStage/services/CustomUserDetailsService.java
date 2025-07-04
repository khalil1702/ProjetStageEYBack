package tn.projetStage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import tn.projetStage.entities.User;
import tn.projetStage.repositories.UserRepository;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Essaie d'abord par CIN
        User user = userRepository.findByCin(username);
        if (user == null) {
            // Sinon, essaie par email
            user = userRepository.findByEmail(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable avec le CIN ou email : " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getCin(), // ou user.getEmail()
                user.getPassword(),
                Collections.emptyList()
        );
    }




}
