package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.mossurd.blooddosyst.entity.Fridge;

import java.util.List;

public interface FridgeRepository extends JpaRepository<Fridge, Integer> {
    List<Fridge> findAllByCreatedBy(String username);
    List<Fridge> findAllByEnabledTrue();
}
