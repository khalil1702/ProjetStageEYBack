package tn.projetStage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.projetStage.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByCin(String cin);

    User findByEmail(String username);
}