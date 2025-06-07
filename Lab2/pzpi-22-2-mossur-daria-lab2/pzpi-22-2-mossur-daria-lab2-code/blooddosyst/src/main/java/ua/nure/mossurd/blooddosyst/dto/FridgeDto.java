package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.Fridge;

public record FridgeDto(
        Integer id,
        String serialNumber,
        String address,
        String notes,
        Float tempCelsiusMin,
        Float tempCelsiusMax,
        Float humidityPercentMin,
        Float humidityPercentMax,
        Boolean enabled
) {
    public FridgeDto(Fridge fridge) {
        this(
                fridge.getId(),
                fridge.getSerialNumber(),
                fridge.getFridgeAddress(),
                fridge.getNotes(),
                fridge.getTempCelsiusMin(),
                fridge.getTempCelsiusMax(),
                fridge.getHumidityPercentMin(),
                fridge.getHumidityPercentMax(),
                fridge.getEnabled()
        );
    }
}
