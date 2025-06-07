package ua.nure.mossurd.blooddosyst.dto;

import java.util.List;

public record SmartSolutionsRequestPayloadDto(
        List<SmartSolutionsRequestDeviceDto> devices
) {
}
