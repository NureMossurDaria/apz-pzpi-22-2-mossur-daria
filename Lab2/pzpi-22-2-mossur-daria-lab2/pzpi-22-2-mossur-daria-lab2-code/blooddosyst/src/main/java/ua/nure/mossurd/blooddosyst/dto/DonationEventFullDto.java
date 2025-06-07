package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.DonationEvent;
import ua.nure.mossurd.blooddosyst.enums.DonationEventStatus;

import java.time.LocalDateTime;

public record DonationEventFullDto(
        Integer id,
        LocalDateTime dateTime,
        String eventAddress,
        DonationEventStatus status,
        String notes,
        Long appointmentsCount
) {
    public DonationEventFullDto(DonationEvent donationEvent, Long appointmentsCount) {
        this(donationEvent.getId(),
                donationEvent.getDateTime(),
                donationEvent.getEventAddress(),
                donationEvent.getEventStatus(),
                donationEvent.getNotes(),
                appointmentsCount);
    }
}
