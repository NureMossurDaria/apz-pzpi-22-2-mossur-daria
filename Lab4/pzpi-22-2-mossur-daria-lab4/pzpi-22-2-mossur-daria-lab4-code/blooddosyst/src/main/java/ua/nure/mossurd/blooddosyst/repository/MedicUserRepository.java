package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.mossurd.blooddosyst.entity.MedicUser;

import java.util.List;

public interface MedicUserRepository extends JpaRepository<MedicUser, Integer> {
    MedicUser getByUser_username(String username);
    List<MedicUser> getAllByHospital_hospitalAddress(String hospitalAddress);
}
