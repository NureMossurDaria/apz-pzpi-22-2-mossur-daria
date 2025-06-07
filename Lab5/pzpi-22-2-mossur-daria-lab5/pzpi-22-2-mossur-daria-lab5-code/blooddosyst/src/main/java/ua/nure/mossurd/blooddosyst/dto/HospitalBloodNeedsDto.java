package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.BloodNeeds;
import ua.nure.mossurd.blooddosyst.entity.Hospital;

public record HospitalBloodNeedsDto(
        Integer id,
        String name,
        String address,
        BloodNeedsDto bloodNeeds) {
    public HospitalBloodNeedsDto(Hospital hospital, BloodNeeds bloodNeeds) {
        this(hospital.getId(), hospital.getName(), hospital.getHospitalAddress(), new BloodNeedsDto(bloodNeeds));
    }
}
