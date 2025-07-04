package tn.projetStage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.projetStage.entities.Equipement;
import tn.projetStage.entities.User;
import tn.projetStage.entities.Role;
import tn.projetStage.services.EquipementService;
import tn.projetStage.repositories.UserRepository;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/auth/api/equipements")
@CrossOrigin(origins = "http://localhost:4200")
public class EquipementController {

    @Autowired
    private EquipementService equipementService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Equipement> getAll() {
        return equipementService.getAllEquipements();
    }

    @GetMapping("/{id}")
    public Equipement getById(@PathVariable Long id) {
        return equipementService.getEquipementById(id);
    }



    @PostMapping("/add")
    public Equipement save(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Equipement equipement) {
        User user = userRepository.findByCin(userDetails.getUsername());
        if (user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.CHEF_SERVICE_MAINTENANCE || user.getRole() == Role.TECHNICIEN_MAINTENANCE)) {
            equipement.setUsers(Set.of(user));
            return equipementService.saveEquipement(equipement);
        } else {
            throw new RuntimeException("Accès refusé : seul un ADMIN ou un TECHNICIEN peut ajouter un équipement.");
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        equipementService.deleteEquipement(id);
    }
}
