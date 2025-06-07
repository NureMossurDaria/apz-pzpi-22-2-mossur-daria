package ua.nure.mossurd.blooddosyst.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.mossurd.blooddosyst.dto.HospitalBloodNeedsDto;
import ua.nure.mossurd.blooddosyst.dto.HospitalDto;
import ua.nure.mossurd.blooddosyst.dto.MedicCreationDto;
import ua.nure.mossurd.blooddosyst.dto.MedicDto;
import ua.nure.mossurd.blooddosyst.entity.BloodNeeds;
import ua.nure.mossurd.blooddosyst.entity.Hospital;
import ua.nure.mossurd.blooddosyst.entity.MedicUser;
import ua.nure.mossurd.blooddosyst.enums.UserRole;
import ua.nure.mossurd.blooddosyst.repository.BloodNeedsRepository;
import ua.nure.mossurd.blooddosyst.repository.HospitalRepository;
import ua.nure.mossurd.blooddosyst.repository.MedicUserRepository;
import ua.nure.mossurd.blooddosyst.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final HospitalRepository hospitalRepository;
    private final BloodNeedsRepository bloodNeedsRepository;
    private final UserRepository userRepository;
    private final MedicUserRepository medicUserRepository;
    private final AuthService authService;

    public List<HospitalDto> getAllHospitals() {
        return hospitalRepository.findAll()
                .stream()
                .map(HospitalDto::new)
                .toList();
    }

    public HospitalBloodNeedsDto createHospital(HospitalBloodNeedsDto hospitalBloodNeedsDto) {
        Hospital hospital = new Hospital();
        hospital.setName(hospitalBloodNeedsDto.name());
        hospital.setHospitalAddress(hospitalBloodNeedsDto.address());
        Hospital newHospital = hospitalRepository.saveAndFlush(hospital);
        BloodNeeds bloodNeeds = new BloodNeeds();
        bloodNeeds.setHospital(newHospital);
        BloodNeeds newBloodNeeds = bloodNeedsRepository.saveAndFlush(bloodNeeds);
        return new HospitalBloodNeedsDto(newHospital, newBloodNeeds);
    }

    public HospitalBloodNeedsDto updateHospital(Integer id, HospitalBloodNeedsDto hospitalBloodNeedsDto) {
        Hospital hospital = hospitalRepository.getReferenceById(id);
        hospital.setName(hospitalBloodNeedsDto.name());
        hospital.setHospitalAddress(hospitalBloodNeedsDto.address());
        Hospital newHospital = hospitalRepository.saveAndFlush(hospital);
        BloodNeeds bloodNeeds = bloodNeedsRepository.getByHospitalId(newHospital.getId());
        return new HospitalBloodNeedsDto(newHospital, bloodNeeds);
    }

    public MedicDto createMedic(MedicCreationDto medicCreationDto) {
        authService.createUser(medicCreationDto.username(), UserRole.MEDIC);
        MedicUser medicUser = new MedicUser();
        medicUser.setUser(userRepository.getReferenceById(medicCreationDto.username()));
        medicUser.setHospital(hospitalRepository.getReferenceById(medicCreationDto.hospitalId()));
        medicUser.setFirstName(medicCreationDto.firstName());
        medicUser.setLastName(medicCreationDto.lastName());
        medicUser.setPhoneNumber(medicCreationDto.phone());
        MedicUser newMedicUser = medicUserRepository.saveAndFlush(medicUser);
        return new MedicDto(newMedicUser);
    }

    public MedicDto updateMedic(Integer id, MedicCreationDto medicCreationDto) {
        MedicUser medicUser = medicUserRepository.getReferenceById(id);
        medicUser.setFirstName(medicCreationDto.firstName());
        medicUser.setLastName(medicCreationDto.lastName());
        medicUser.setPhoneNumber(medicCreationDto.phone());
        medicUser.setHospital(hospitalRepository.getReferenceById(medicCreationDto.hospitalId()));
        MedicUser newMedicUser = medicUserRepository.saveAndFlush(medicUser);
        return new MedicDto(newMedicUser);
    }
}
