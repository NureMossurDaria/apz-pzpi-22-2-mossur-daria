package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.DonorData;
import ua.nure.mossurd.blooddosyst.enums.BloodType;

public record DonorDto(
        Integer id,
        String username,
        String firstName,
        String lastName,
        BloodType bloodType,
        Boolean rhesus,
        String phone
) {
    public DonorDto(DonorData donorData) {
        this(
                donorData.getId(),
                donorData.getUser().getUsername(),
                donorData.getFirstName(),
                donorData.getLastName(),
                donorData.getBloodType(),
                donorData.getRhesusFactor(),
                donorData.getPhoneNumber()
        );
    }
}
