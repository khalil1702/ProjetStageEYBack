// InterventionService.java
package tn.projetStage.services;

import tn.projetStage.entities.Intervention;
import java.util.List;

public interface InterventionService {
    List<Intervention> getAllInterventions();
    Intervention getInterventionById(Long id);
    Intervention saveIntervention(Intervention intervention);
    void deleteIntervention(Long id);
}
