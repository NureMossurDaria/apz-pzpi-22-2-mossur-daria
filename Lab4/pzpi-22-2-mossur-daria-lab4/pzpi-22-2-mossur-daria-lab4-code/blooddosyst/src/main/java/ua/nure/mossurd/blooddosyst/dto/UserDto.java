package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.enums.Language;
import ua.nure.mossurd.blooddosyst.enums.UserRole;

import java.util.List;

public record UserDto(
        String name,
        String token,
        List<UserRole> roles,
        Language language,
        Boolean piAgreed
) {
}
