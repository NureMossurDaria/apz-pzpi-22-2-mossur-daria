package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.FridgeMetric;

import java.time.LocalDateTime;

public record FridgeMetricDto(
        Integer id,
        Integer fridgeId,
        Float tempCelsius,
        Float humidityPercent,
        LocalDateTime dateTime) {
    public FridgeMetricDto(FridgeMetric metric) {
        this(
                metric.getId(),
                metric.getFridge().getId(),
                metric.getTempCelsius(),
                metric.getHumidityPercent(),
                metric.getDateTime()
        );
    }
}
