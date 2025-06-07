package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.mossurd.blooddosyst.entity.DonorData;

public interface DonorDataRepository extends JpaRepository<DonorData, Integer> {
    DonorData getByUser_username(String username);
}
