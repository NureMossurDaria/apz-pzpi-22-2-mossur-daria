package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.nure.mossurd.blooddosyst.entity.DonationAppointment;
import ua.nure.mossurd.blooddosyst.entity.DonationEvent;
import ua.nure.mossurd.blooddosyst.entity.DonorData;

import java.util.List;

public interface DonationAppointmentRepository extends JpaRepository<DonationAppointment, Integer> {
    long countByDonationEvent_id(Integer id);
    List<DonationAppointment> getAllByDonorData(DonorData donorData);
    List<DonationAppointment> getAllByDonorDataAndDonationEvent_id(DonorData donorData, Integer eventId);
    @Transactional
    void deleteAllByDonorDataAndDonationEvent_id(DonorData donorData, Integer eventId);
    @Transactional
    void deleteAllByDonorData(DonorData donorData);
    List<DonationAppointment> getAllByDonationEventIn(List<DonationEvent> donationEvents);
}
