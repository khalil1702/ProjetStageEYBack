package tn.projetStage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.projetStage.entities.Intervention;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {}

