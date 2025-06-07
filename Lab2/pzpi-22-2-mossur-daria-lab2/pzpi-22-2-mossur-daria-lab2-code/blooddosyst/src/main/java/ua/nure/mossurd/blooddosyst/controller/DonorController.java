package ua.nure.mossurd.blooddosyst.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.nure.mossurd.blooddosyst.dto.DonationEventDto;
import ua.nure.mossurd.blooddosyst.dto.DonorDto;
import ua.nure.mossurd.blooddosyst.service.DonorService;

import java.util.List;

@RestController
@RequestMapping(value = "/donor")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService donorService;

    @GetMapping("/self")
    public DonorDto getSelfInfo() {
        return donorService.getSelfInfo();
    }

    @GetMapping("/event/available")
    public List<DonationEventDto> getAvailableEvents() {
        return donorService.getAvailableEvents();
    }

    @GetMapping("/event/appointment/all")
    public List<DonationEventDto> getDonationEventsWithAppointments() {
        return donorService.getEventsWithAppointments();
    }

    @PostMapping("/event/{id}/appointment/new")
    public void createAppointmentForEvent(@PathVariable("id") Integer id) {
        donorService.createAppointment(id);
    }

    @DeleteMapping("/event/{id}/appointment/delete")
    public void deleteAppointmentForEvent(@PathVariable("id") Integer id) {
        donorService.deleteAppointment(id);
    }

    @GetMapping("/donations")
    public List<DonationEventDto> getDonationsForDonor() {
        return donorService.getDonationsForDonor();
    }
}
