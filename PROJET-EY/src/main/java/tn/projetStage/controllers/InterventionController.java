package tn.projetStage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.projetStage.entities.Intervention;
import tn.projetStage.entities.User;
import tn.projetStage.entities.Equipement;
import tn.projetStage.entities.Role;
import tn.projetStage.repositories.UserRepository;
import tn.projetStage.repositories.EquipementRepository;
import tn.projetStage.repositories.InterventionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/interventions")
@CrossOrigin(origins = "http://localhost:4200")
public class InterventionController {

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipementRepository equipementRepository;

    @GetMapping
    public List<Intervention> getAll() {
        return interventionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Intervention getById(@PathVariable Long id) {
        return interventionRepository.findById(id).orElse(null);
    }

    @PostMapping("/add/{userId}/{equipementId}")
    public Intervention save(@PathVariable Long userId,
                             @PathVariable Long equipementId,
                             @RequestBody Intervention intervention) {
        User user = userRepository.findById(userId).orElse(null);
        Equipement equipement = equipementRepository.findById(equipementId).orElse(null);

        if (user != null && equipement != null &&
                (user.getRole() == Role.ADMIN || user.getRole() == Role.CHEF_SERVICE_MAGASIN)) {

            intervention.setUser(user);
            intervention.setEquipement(equipement);
            return interventionRepository.save(intervention);
        } else {
            throw new RuntimeException("Accès refusé : seul un ADMIN ou un TECHNICIEN peut faire une intervention.");
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        interventionRepository.deleteById(id);
    }
}
