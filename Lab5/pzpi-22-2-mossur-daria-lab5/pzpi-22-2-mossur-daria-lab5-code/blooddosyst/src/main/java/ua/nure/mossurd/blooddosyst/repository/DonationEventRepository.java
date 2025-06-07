package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.mossurd.blooddosyst.entity.DonationEvent;
import ua.nure.mossurd.blooddosyst.enums.DonationEventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface DonationEventRepository extends JpaRepository<DonationEvent, Integer> {
    List<DonationEvent> getByEventStatusInAndDateTimeAfter(List<DonationEventStatus> statuses, LocalDateTime dateTime);
    List<DonationEvent> getByEventStatusInAndDateTimeBetween(List<DonationEventStatus> statuses, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);
}
