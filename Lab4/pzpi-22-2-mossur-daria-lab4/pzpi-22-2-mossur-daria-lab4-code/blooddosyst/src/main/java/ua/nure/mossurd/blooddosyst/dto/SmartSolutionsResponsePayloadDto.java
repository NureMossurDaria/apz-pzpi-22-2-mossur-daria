package ua.nure.mossurd.blooddosyst.dto;

import java.util.Map;

public record SmartSolutionsResponsePayloadDto(
        Map<String, SmartSolutionsResponseDeviceDto> devices
) {
}
