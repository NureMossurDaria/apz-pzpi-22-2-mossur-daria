package ua.nure.mossurd.blooddosyst.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ua.nure.mossurd.blooddosyst.dto.DonationEventDto;
import ua.nure.mossurd.blooddosyst.dto.DonorDto;
import ua.nure.mossurd.blooddosyst.entity.Donation;
import ua.nure.mossurd.blooddosyst.entity.DonationAppointment;
import ua.nure.mossurd.blooddosyst.entity.DonationEvent;
import ua.nure.mossurd.blooddosyst.enums.DonationEventStatus;
import ua.nure.mossurd.blooddosyst.repository.DonationAppointmentRepository;
import ua.nure.mossurd.blooddosyst.repository.DonationEventRepository;
import ua.nure.mossurd.blooddosyst.repository.DonationRepository;
import ua.nure.mossurd.blooddosyst.repository.DonorDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorDataRepository donorDataRepository;
    private final DonationEventRepository donationEventRepository;
    private final DonationAppointmentRepository donationAppointmentRepository;
    private final DonationRepository donationRepository;

    public DonorDto getSelfInfo() {
        return new DonorDto(donorDataRepository.getByUser_username((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }

    public List<DonationEventDto> getAvailableEvents() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return donationEventRepository
                .getByEventStatusInAndDateTimeAfter(List.of(DonationEventStatus.PLANNED, DonationEventStatus.ONGOING), LocalDateTime.now()).stream()
                .filter(event -> CollectionUtils.isEmpty(donationAppointmentRepository.getAllByDonorDataAndDonationEvent_id(donorDataRepository.getByUser_username(username), event.getId())))
                .map(DonationEventDto::new)
                .toList();
    }

    public List<DonationEventDto> getEventsWithAppointments() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return donationEventRepository.findAllById(
                        donationAppointmentRepository.getAllByDonorData(donorDataRepository.getByUser_username(username))
                                .stream().map(DonationAppointment::getDonationEvent).map(DonationEvent::getId).toList()).stream()
                .map(DonationEventDto::new)
                .toList();
    }

    public void createAppointment(Integer eventId) {
        DonationAppointment donationAppointment = new DonationAppointment();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        donationAppointment.setDonorData(donorDataRepository.getByUser_username(username));
        donationAppointment.setDonationEvent(donationEventRepository.getReferenceById(eventId));
        donationAppointmentRepository.saveAndFlush(donationAppointment);
    }

    public void deleteAppointment(Integer eventId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        donationAppointmentRepository.deleteAllByDonorDataAndDonationEvent_id(donorDataRepository.getByUser_username(username), eventId);
        donationAppointmentRepository.flush();
    }

    public List<DonationEventDto> getDonationsForDonor() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return donationRepository.getAllByDonorData(donorDataRepository.getByUser_username(username)).stream()
                .map(Donation::getDonationEvent)
                .map(DonationEventDto::new)
                .toList();
    }
}
