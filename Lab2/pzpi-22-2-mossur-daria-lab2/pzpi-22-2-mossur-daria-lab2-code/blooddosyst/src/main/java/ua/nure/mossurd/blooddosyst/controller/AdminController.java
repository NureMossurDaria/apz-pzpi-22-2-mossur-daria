package ua.nure.mossurd.blooddosyst.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.mossurd.blooddosyst.dto.*;
import ua.nure.mossurd.blooddosyst.service.AdminService;

import java.util.List;

@RestController
@RequestMapping(value = "/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Hospital operations

    @GetMapping("/hospital/all")
    public List<HospitalDto> getAllHospitals() {
        return adminService.getAllHospitals();
    }

    @PostMapping("/hospital/new")
    public HospitalBloodNeedsDto createHospital(@RequestBody HospitalBloodNeedsDto hospitalBloodNeedsDto) {
        return adminService.createHospital(hospitalBloodNeedsDto);
    }

    @PutMapping("/hospital/{id}/update")
    public HospitalBloodNeedsDto updateHospital(@PathVariable("id") Integer id, @RequestBody HospitalBloodNeedsDto hospitalBloodNeedsDto) {
        return adminService.updateHospital(id, hospitalBloodNeedsDto);
    }

    @DeleteMapping("/hospital/{id}/delete")
    public void deleteHospital(@PathVariable("id") Integer id) {
        throw new ResponseStatusException(HttpStatusCode.valueOf(500), "coming_soon");
    }

    // User operations

    @PostMapping("/medic/new")
    public MedicDto createMedic(@RequestBody MedicCreationDto medicCreationDto) {
        return adminService.createMedic(medicCreationDto);
    }

    @PutMapping("/medic/{id}/update")
    public MedicDto updateMedic(@PathVariable("id") Integer id, @RequestBody MedicCreationDto medicCreationDto) {
        return adminService.updateMedic(id, medicCreationDto);
    }
}
