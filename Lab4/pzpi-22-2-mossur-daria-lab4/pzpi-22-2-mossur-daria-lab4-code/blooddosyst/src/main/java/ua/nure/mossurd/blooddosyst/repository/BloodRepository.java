package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.mossurd.blooddosyst.entity.Blood;
import ua.nure.mossurd.blooddosyst.entity.Fridge;
import ua.nure.mossurd.blooddosyst.entity.User;

import java.util.List;

public interface BloodRepository extends JpaRepository<Blood, Integer> {
    List<Blood> findAllByFridge(Fridge fridge);
    List<Blood> findAllByCreatedBy(String username);
    Blood findByDonation_id(Integer id);
}
