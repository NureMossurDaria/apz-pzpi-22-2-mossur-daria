package ua.nure.mossurd.blooddosyst.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.nure.mossurd.blooddosyst.dto.*;
import ua.nure.mossurd.blooddosyst.enums.BloodStatus;
import ua.nure.mossurd.blooddosyst.service.MedicService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/medic")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MedicController {

    private final MedicService medicService;

    @GetMapping("/self")
    public MedicDto getSelfInfo() {
        return medicService.getSelfInfo();
    }

    // Blood needs operations

    @GetMapping("/hospital/needs")
    public BloodNeedsDto getBloodNeeds() {
        return medicService.getBloodNeeds();
    }

    @PutMapping("/hospital/needs/update")
    public BloodNeedsDto updateBloodNeeds(@RequestBody BloodNeedsDto bloodNeedsDto) {
        return medicService.updateBloodNeeds(bloodNeedsDto);
    }

    // Donor operations

    @PostMapping("/donor/new")
    public DonorDto createDonor(@RequestBody DonorDto donorDto) {
        return medicService.createDonor(donorDto);
    }

    @PutMapping("/donor/{id}/update")
    public DonorDto updateDonor(@PathVariable("id") Integer id, @RequestBody DonorDto donorDto) {
        return medicService.updateDonor(id, donorDto);
    }

    @DeleteMapping("/donor/{id}/delete")
    public void deleteDonor(@PathVariable("id") Integer id) {
        medicService.deleteDonor(id);
    }

    @GetMapping("/donor/username/{username}")
    public DonorDto getDonorByUsername(@PathVariable("username") String username) {
        return medicService.getDonorByUsername(username);
    }

    // Donation event operations

    @GetMapping("/event/all")
    public List<DonationEventFullDto> getAllEvents() {
        return medicService.getAllEvents();
    }

    @GetMapping("/event/{id}")
    public DonationEventFullDto getDonationEvent(@PathVariable("id") Integer id) {
        return medicService.getEvent(id);
    }

    @PostMapping("/event/new")
    public DonationEventDto createDonationEvent(@RequestBody DonationEventDto eventDto) {
        return medicService.createEvent(eventDto);
    }

    @PutMapping("/event/{id}/update")
    public DonationEventDto updateDonationEvent(@PathVariable("id") Integer id, @RequestBody DonationEventDto donationEventDto) {
        return medicService.updateEvent(id, donationEventDto);
    }

    // Donation appointments operations

    @GetMapping("/appointments/event/{id}")
    public Long getAppointmentsQuantity(@PathVariable("id") Integer id) {
        return medicService.getAppointmentQuantityForEvent(id);
    }

    // Fridge operations

    @GetMapping("/fridge/all")
    public List<FridgeDto> getAllFridges() {
        return medicService.getAllFridges();
    }

    @GetMapping("/fridge/{id}")
    public FridgeDto getFridge(@PathVariable("id") Integer id) {
        return medicService.getFridge(id);
    }

    @PostMapping("/fridge/new")
    public FridgeDto createFridge(@RequestBody FridgeDto fridgeDto) {
        return medicService.createFridge(fridgeDto);
    }

    @PutMapping("/fridge/{id}/update")
    public FridgeDto updateFridge(@PathVariable("id") Integer id, @RequestBody FridgeDto fridgeDto) {
        return medicService.updateFridge(id, fridgeDto);
    }

    @GetMapping("/fridge/{id}/metrics/from/{startDate}/to/{endDate}")
    public List<FridgeMetricDto> getMetricsForFridge(@PathVariable("id") Integer id,
                                                     @PathVariable("startDate") LocalDate startDate,
                                                     @PathVariable("endDate") LocalDate endDate) {
        return medicService.getFridgeMetrics(id, LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                LocalDateTime.of(endDate, LocalTime.MIDNIGHT));
    }

    // Donation operations

    @GetMapping("/donation/{id}")
    public DonationDto getDonation(@PathVariable("id") Integer id) {
        return medicService.getDonation(id);
    }

    @PostMapping("/donation/new")
    public DonationDto createDonation(@RequestBody DonationDto donationDto) {
        return medicService.createDonation(donationDto);
    }

    // Blood operations

    @GetMapping("/blood/all")
    public List<BloodFullDto> getAllBlood() {
        return medicService.getAllBlood();
    }

    @GetMapping("/blood/{id}")
    public BloodFullDto getBlood(@PathVariable("id") Integer id) {
        return medicService.getBlood(id);
    }

    @PutMapping("/blood/{id}/status/{status}")
    public BloodDto updateBloodStatus(@PathVariable("id") Integer id, @PathVariable("status") BloodStatus status) {
        return medicService.updateBloodStatus(id, status);
    }

    @PutMapping("/blood/{id}/spoil")
    public BloodDto updateBloodSpoiled(@PathVariable("id") Integer id) {
        return medicService.updateBloodSpoiled(id, true);
    }
}
