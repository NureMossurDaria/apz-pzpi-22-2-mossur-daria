package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.Blood;
import ua.nure.mossurd.blooddosyst.entity.Fridge;
import ua.nure.mossurd.blooddosyst.enums.BloodStatus;
import ua.nure.mossurd.blooddosyst.enums.BloodType;

public record BloodFullDto(
        Integer id,
        BloodType bloodType,
        Boolean rhesus,
        Boolean spoiled,
        String barcode,
        BloodStatus status,
        FridgeDto fridge
) {
    public BloodFullDto(Blood blood, Fridge fridge) {
        this(
                blood.getId(),
                blood.getBloodType(),
                blood.getRhesusFactor(),
                blood.getSpoiled(),
                blood.getBarcode(),
                blood.getUseStatus(),
                new FridgeDto(fridge)
        );
    }
}
