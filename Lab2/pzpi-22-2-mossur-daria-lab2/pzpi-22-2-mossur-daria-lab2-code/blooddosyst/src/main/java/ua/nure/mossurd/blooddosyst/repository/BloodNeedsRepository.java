package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.mossurd.blooddosyst.entity.BloodNeeds;

public interface BloodNeedsRepository extends JpaRepository<BloodNeeds, Integer> {

    BloodNeeds getByHospitalId(Integer hospitalId);
}
