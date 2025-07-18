// EquipementService.java
package tn.projetStage.services;

import tn.projetStage.entities.Equipement;
import java.util.List;

public interface EquipementService {
    List<Equipement> getAllEquipements();
    Equipement getEquipementById(Long id);
    Equipement saveEquipement(Equipement equipement);
    void deleteEquipement(Long id);

    void updateEquipement(Long id, Equipement updatedEquipement);
}
