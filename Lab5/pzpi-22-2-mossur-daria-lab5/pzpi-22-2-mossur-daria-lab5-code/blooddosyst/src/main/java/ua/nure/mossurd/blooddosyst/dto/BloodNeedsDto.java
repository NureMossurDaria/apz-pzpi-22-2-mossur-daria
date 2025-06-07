package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.BloodNeeds;

public record BloodNeedsDto(
        Float oNegative,
        Float oPositive,
        Float aNegative,
        Float aPositive,
        Float bNegative,
        Float bPositive,
        Float abNegative,
        Float abPositive
) {
    public BloodNeedsDto(BloodNeeds bloodNeeds) {
        this(bloodNeeds.getONegative(),
                bloodNeeds.getOPositive(),
                bloodNeeds.getANegative(),
                bloodNeeds.getAPositive(),
                bloodNeeds.getBNegative(),
                bloodNeeds.getBPositive(),
                bloodNeeds.getAbNegative(),
                bloodNeeds.getAbPositive());
    }
}
