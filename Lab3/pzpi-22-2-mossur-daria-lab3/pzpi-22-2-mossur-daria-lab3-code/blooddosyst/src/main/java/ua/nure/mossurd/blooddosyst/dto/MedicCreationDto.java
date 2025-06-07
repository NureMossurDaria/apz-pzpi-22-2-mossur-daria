package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.MedicUser;

public record MedicCreationDto(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String phone,
        Integer hospitalId
) {
    public MedicCreationDto(MedicUser medicUser) {
        this(
                medicUser.getId(),
                medicUser.getUser().getUsername(),
                medicUser.getFirstName(),
                medicUser.getLastName(),
                medicUser.getPhoneNumber(),
                medicUser.getHospital().getId()
        );
    }
}
