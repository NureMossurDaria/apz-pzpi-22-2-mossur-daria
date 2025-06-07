package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.Blood;
import ua.nure.mossurd.blooddosyst.enums.BloodStatus;
import ua.nure.mossurd.blooddosyst.enums.BloodType;

public record BloodDto(
        Integer id,
        BloodType bloodType,
        Boolean rhesus,
        Boolean spoiled,
        String barcode,
        BloodStatus status,
        Integer fridgeId
) {
    public BloodDto(Blood blood) {
        this(
                blood.getId(),
                blood.getBloodType(),
                blood.getRhesusFactor(),
                blood.getSpoiled(),
                blood.getBarcode(),
                blood.getUseStatus(),
                blood.getFridge().getId()
        );
    }
}
