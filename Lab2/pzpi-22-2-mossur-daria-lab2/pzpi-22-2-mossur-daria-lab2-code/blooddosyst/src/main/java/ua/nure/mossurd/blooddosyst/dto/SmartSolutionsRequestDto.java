package ua.nure.mossurd.blooddosyst.dto;

import java.util.List;

public record SmartSolutionsRequestDto(
        String requestId,
        List<SmartSolutionsInputDto> inputs
) {
}
