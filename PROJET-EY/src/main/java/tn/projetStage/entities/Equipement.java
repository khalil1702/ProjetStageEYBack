package tn.projetStage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private Etat etat;

    private String type;
    private String localisation;
    private String categorie;
    private String fournisseur;
    private String serviceAffecte;
    private Date dateMiseEnService;
    private Date dateProchaineMaintenance;

    private String modele;

    private LocalDate dateAcquisition;

    @OneToMany(mappedBy = "equipement", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Intervention> interventions;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "equipement_user",
            joinColumns = @JoinColumn(name = "equipement_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
