// InterventionServiceImpl.java
package tn.projetStage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.projetStage.entities.Intervention;
import tn.projetStage.repositories.InterventionRepository;

import java.util.List;

@Service
public class InterventionServiceImpl implements InterventionService {

    @Autowired
    private InterventionRepository interventionRepository;

    @Override
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    public Intervention getInterventionById(Long id) {
        return interventionRepository.findById(id).orElse(null);
    }

    @Override
    public Intervention saveIntervention(Intervention intervention) {
        return interventionRepository.save(intervention);
    }

    @Override
    public void deleteIntervention(Long id) {
        interventionRepository.deleteById(id);
    }
}
