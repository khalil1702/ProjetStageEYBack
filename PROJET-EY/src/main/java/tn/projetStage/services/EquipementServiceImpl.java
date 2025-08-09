// EquipementServiceImpl.java
package tn.projetStage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.projetStage.entities.Equipement;
import tn.projetStage.entities.Etat;
import tn.projetStage.repositories.EquipementRepository;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class EquipementServiceImpl implements EquipementService {

    @Autowired
    private EquipementRepository equipementRepository;

    @Override
    public List<Equipement> getAllEquipements() {
        return equipementRepository.findAll();
    }

    @Override
    public Equipement getEquipementById(Long id) {
        return equipementRepository.findById(id).orElse(null);
    }

    @Override
    public Equipement saveEquipement(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    @Override
    public void deleteEquipement(Long id) {
        equipementRepository.deleteById(id);
    }

    @Override
    public void updateEquipement(Long id, Equipement updatedEquipement) {

    }
    @Override
    public List<Equipement> getDerniersEquipementsEnMaintenance(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return equipementRepository.findByEtatOrderByDateProchaineMaintenanceDesc(Etat.EN_MAINTENANCE, pageable);
    }

}
