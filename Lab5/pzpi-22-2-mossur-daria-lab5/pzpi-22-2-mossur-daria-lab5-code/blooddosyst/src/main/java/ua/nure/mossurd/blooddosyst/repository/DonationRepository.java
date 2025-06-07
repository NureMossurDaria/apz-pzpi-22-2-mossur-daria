package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.mossurd.blooddosyst.entity.Donation;
import ua.nure.mossurd.blooddosyst.entity.DonorData;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Integer> {
    List<Donation> getAllByDonorData(DonorData donorData);
}
