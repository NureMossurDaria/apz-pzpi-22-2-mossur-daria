package ua.nure.mossurd.blooddosyst.dto;

public record SmartSolutionsResponseDeviceDto(
        String status,
        Boolean online,
        Float temperatureSetpointCelsius,
        Float temperatureAmbientCelsius,
        Float humiditySetpointPercent,
        Float humidityAmbientPercent
) {
}
