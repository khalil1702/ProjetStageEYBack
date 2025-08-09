package tn.projetStage.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.projetStage.entities.Equipement;
import tn.projetStage.entities.Etat;

import java.util.List;

public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    List<Equipement> findByEtatOrderByDateProchaineMaintenanceDesc(Etat etat, Pageable pageable);
}



