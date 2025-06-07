package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.Hospital;

public record HospitalDto(
        Integer id,
        String name,
        String address
) {
    public HospitalDto(Hospital hospital) {
        this(hospital.getId(), hospital.getName(), hospital.getHospitalAddress());
    }
}
