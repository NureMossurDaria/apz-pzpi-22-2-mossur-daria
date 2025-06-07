package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.Blood;
import ua.nure.mossurd.blooddosyst.entity.Donation;
import ua.nure.mossurd.blooddosyst.entity.DonorData;

import java.util.Optional;

public record DonationDto(
        Integer id,
        Integer donorId,
        Integer eventId,
        BloodDto blood
) {
    public DonationDto(Donation donation, Blood blood) {
        this(
                donation.getId(),
                Optional.ofNullable(donation.getDonorData()).map(DonorData::getId).orElse(null),
                donation.getDonationEvent().getId(),
                new BloodDto(blood)
        );
    }
}
