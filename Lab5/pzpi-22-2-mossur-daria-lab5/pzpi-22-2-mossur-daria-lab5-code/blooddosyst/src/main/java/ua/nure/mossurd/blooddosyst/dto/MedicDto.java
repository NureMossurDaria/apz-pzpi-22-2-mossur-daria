package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.MedicUser;

public record MedicDto(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String phone,
        HospitalDto hospital
) {
    public MedicDto(MedicUser medicUser) {
        this(
                medicUser.getId(),
                medicUser.getUser().getUsername(),
                medicUser.getFirstName(),
                medicUser.getLastName(),
                medicUser.getPhoneNumber(),
                new HospitalDto(medicUser.getHospital())
        );
    }
}
