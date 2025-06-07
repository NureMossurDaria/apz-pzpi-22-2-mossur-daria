package ua.nure.mossurd.blooddosyst.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.nure.mossurd.blooddosyst.dto.*;
import ua.nure.mossurd.blooddosyst.entity.*;
import ua.nure.mossurd.blooddosyst.enums.BloodStatus;
import ua.nure.mossurd.blooddosyst.enums.UserRole;
import ua.nure.mossurd.blooddosyst.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicService {

    private static final float BLOOD_AMOUNT_PER_DONATION = 0.45f;

    private final BloodNeedsRepository bloodNeedsRepository;
    private final MedicUserRepository medicUserRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final DonorDataRepository donorDataRepository;
    private final DonationAppointmentRepository donationAppointmentRepository;
    private final DonationRepository donationRepository;
    private final NotificationRepository notificationRepository;
    private final BloodRepository bloodRepository;
    private final FridgeRepository fridgeRepository;
    private final DonationEventRepository donationEventRepository;
    private final FridgeMetricsRepository fridgeMetricsRepository;

    public MedicDto getSelfInfo() {
        return new MedicDto(medicUserRepository.getByUser_username((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }

    public BloodNeedsDto getBloodNeeds() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MedicUser medic = medicUserRepository.getByUser_username(username);
        return new BloodNeedsDto(bloodNeedsRepository.getByHospitalId(medic.getHospital().getId()));
    }

    public BloodNeedsDto updateBloodNeeds(BloodNeedsDto bloodNeedsDto) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MedicUser medic = medicUserRepository.getByUser_username(username);
        BloodNeeds bloodNeeds = bloodNeedsRepository.getByHospitalId(medic.getHospital().getId());
        bloodNeeds.setONegative(bloodNeedsDto.oNegative());
        bloodNeeds.setOPositive(bloodNeedsDto.oPositive());
        bloodNeeds.setANegative(bloodNeedsDto.aNegative());
        bloodNeeds.setAPositive(bloodNeedsDto.aPositive());
        bloodNeeds.setBNegative(bloodNeedsDto.bNegative());
        bloodNeeds.setBPositive(bloodNeedsDto.bPositive());
        bloodNeeds.setAbNegative(bloodNeedsDto.abNegative());
        bloodNeeds.setAbPositive(bloodNeedsDto.abPositive());
        return new BloodNeedsDto(bloodNeedsRepository.saveAndFlush(bloodNeeds));
    }

    public DonorDto createDonor(DonorDto donorDto) {
        authService.createUser(donorDto.username(), UserRole.DONOR);
        DonorData donorData = new DonorData();
        donorData.setUser(userRepository.getReferenceById(donorDto.username()));
        donorData.setFirstName(donorDto.firstName());
        donorData.setLastName(donorDto.lastName());
        donorData.setPhoneNumber(donorDto.phone());
        donorData.setBloodType(donorDto.bloodType());
        donorData.setRhesusFactor(donorDto.rhesus());
        return new DonorDto(donorDataRepository.saveAndFlush(donorData));
    }

    public DonorDto updateDonor(Integer id, DonorDto donorDto) {
        DonorData donorData = donorDataRepository.getReferenceById(id);
        donorData.setFirstName(donorDto.firstName());
        donorData.setLastName(donorDto.lastName());
        donorData.setPhoneNumber(donorDto.phone());
        donorData.setBloodType(donorDto.bloodType());
        donorData.setRhesusFactor(donorDto.rhesus());
        return new DonorDto(donorDataRepository.saveAndFlush(donorData));
    }

    public void deleteDonor(Integer id) {
        DonorData donorData = donorDataRepository.getReferenceById(id);
        donationAppointmentRepository.deleteAllByDonorData(donorData);
        donationAppointmentRepository.flush();
        List<Donation> donations = donationRepository.getAllByDonorData(donorData);
        donations.forEach(donation -> donation.setDonorData(null));
        donationRepository.saveAllAndFlush(donations);

        User user = donorData.getUser();
        List<Notification> notificationsByCreatedBy = notificationRepository.getAllByCreatedBy(user.getUsername());
        notificationsByCreatedBy.forEach(notification -> notification.setCreatedBy(null));
        notificationRepository.saveAllAndFlush(notificationsByCreatedBy);
        notificationRepository.deleteAllByTargetUser(user);
        notificationRepository.flush();
        List<Blood> bloodByCreatedBy = bloodRepository.findAllByCreatedBy(user.getUsername());
        bloodByCreatedBy.forEach(blood -> blood.setCreatedBy(null));
        bloodRepository.saveAllAndFlush(bloodByCreatedBy);
        List<Fridge> fridgeList = fridgeRepository.findAllByCreatedBy(user.getUsername());
        fridgeList.forEach(fridge -> fridge.setCreatedBy(null));
        fridgeRepository.saveAllAndFlush(fridgeList);

        donorDataRepository.delete(donorData);
        donorDataRepository.flush();
        user.setEnabled(false);
        userRepository.saveAndFlush(user);
    }

    public DonorDto getDonorByUsername(String username) {
        return Optional.ofNullable(donorDataRepository.getByUser_username(username))
                .map(DonorDto::new)
                .orElse(null);
    }

    public List<DonationEventFullDto> getAllEvents() {
        return donationEventRepository.findAll().stream()
                .map(e -> new DonationEventFullDto(e, getAppointmentQuantityForEvent(e.getId())))
                .toList();
    }

    public DonationEventFullDto getEvent(Integer id) {
        return new DonationEventFullDto(donationEventRepository.getReferenceById(id), getAppointmentQuantityForEvent(id));
    }

    public DonationEventDto createEvent(DonationEventDto donationEventDto) {
        DonationEvent event = new DonationEvent();
        event.setEventAddress(donationEventDto.eventAddress());
        event.setEventStatus(donationEventDto.status());
        event.setNotes(donationEventDto.notes());
        event.setDateTime(donationEventDto.dateTime());
        event.setCreatedBy((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return new DonationEventDto(donationEventRepository.saveAndFlush(event));
    }

    public DonationEventDto updateEvent(Integer id, DonationEventDto donationEventDto) {
        DonationEvent event = donationEventRepository.getReferenceById(id);
        event.setEventAddress(donationEventDto.eventAddress());
        event.setEventStatus(donationEventDto.status());
        event.setNotes(donationEventDto.notes());
        event.setDateTime(donationEventDto.dateTime());
        return new DonationEventDto(donationEventRepository.saveAndFlush(event));
    }

    public Long getAppointmentQuantityForEvent(Integer eventId) {
        return donationAppointmentRepository.countByDonationEvent_id(eventId);
    }

    public List<FridgeDto> getAllFridges() {
        return fridgeRepository.findAll().stream().map(FridgeDto::new).toList();
    }

    public FridgeDto getFridge(Integer id) {
        return new FridgeDto(fridgeRepository.getReferenceById(id));
    }

    public FridgeDto createFridge(FridgeDto fridgeDto) {
        Fridge fridge = new Fridge();
        fridge.setSerialNumber(fridgeDto.serialNumber());
        fridge.setFridgeAddress(fridgeDto.address());
        fridge.setNotes(fridgeDto.notes());
        fridge.setTempCelsiusMin(fridgeDto.tempCelsiusMin());
        fridge.setTempCelsiusMax(fridgeDto.tempCelsiusMax());
        fridge.setHumidityPercentMin(fridgeDto.humidityPercentMin());
        fridge.setHumidityPercentMax(fridgeDto.humidityPercentMax());
        fridge.setCreatedBy((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return new FridgeDto(fridgeRepository.saveAndFlush(fridge));
    }

    public FridgeDto updateFridge(Integer id, FridgeDto fridgeDto) {
        Fridge fridge = fridgeRepository.getReferenceById(id);
        fridge.setSerialNumber(fridgeDto.serialNumber());
        fridge.setFridgeAddress(fridgeDto.address());
        fridge.setNotes(fridgeDto.notes());
        fridge.setTempCelsiusMin(fridgeDto.tempCelsiusMin());
        fridge.setTempCelsiusMax(fridgeDto.tempCelsiusMax());
        fridge.setHumidityPercentMin(fridgeDto.humidityPercentMin());
        fridge.setHumidityPercentMax(fridgeDto.humidityPercentMax());
        fridge.setEnabled(fridgeDto.enabled());
        return new FridgeDto(fridgeRepository.saveAndFlush(fridge));
    }

    public List<FridgeMetricDto> getFridgeMetrics(Integer id, LocalDateTime startDate, LocalDateTime endDate) {
        return fridgeMetricsRepository.findAllByFridge_idAndDateTimeBetween(id, startDate, endDate.plusDays(1)).stream()
                .map(FridgeMetricDto::new)
                .toList();
    }

    public DonationDto getDonation(Integer id) {
        return new DonationDto(donationRepository.getReferenceById(id), bloodRepository.findByDonation_id(id));
    }

    public DonationDto createDonation(DonationDto donationDto) {
        Donation donation = new Donation();
        donation.setDonationEvent(donationEventRepository.getReferenceById(donationDto.eventId()));
        donation.setDonorData(donorDataRepository.getReferenceById(donationDto.donorId()));
        String createdBy = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        donation.setCreatedBy(createdBy);
        Donation newDonation = donationRepository.saveAndFlush(donation);
        Blood blood = new Blood();
        blood.setBloodType(donation.getDonorData().getBloodType());
        blood.setRhesusFactor(donation.getDonorData().getRhesusFactor());
        blood.setBarcode(donationDto.blood().barcode());
        blood.setCreatedBy(createdBy);
        blood.setDonation(newDonation);
        blood.setFridge(fridgeRepository.getReferenceById(donationDto.blood().fridgeId()));
        return new DonationDto(newDonation, bloodRepository.saveAndFlush(blood));
    }

    public List<BloodFullDto> getAllBlood() {
        return bloodRepository.findAll().stream()
                .map(b -> new BloodFullDto(b, b.getFridge()))
                .toList();
    }

    public BloodFullDto getBlood(Integer id) {
        Blood blood = bloodRepository.getReferenceById(id);
        return new BloodFullDto(blood, blood.getFridge());
    }

    public BloodDto updateBloodStatus(Integer id, BloodStatus status) {
        Blood blood = bloodRepository.getReferenceById(id);
        blood.setUseStatus(status);
        if (!blood.getSpoiled() && status == BloodStatus.RESERVED) {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MedicUser medic = medicUserRepository.getByUser_username(username);
            BloodNeeds bloodNeeds = bloodNeedsRepository.getByHospitalId(medic.getHospital().getId());
            switch (blood.getBloodType()) {
                case O -> {
                    if (blood.getRhesusFactor()) {
                        bloodNeeds.setOPositive(bloodNeeds.getOPositive() - BLOOD_AMOUNT_PER_DONATION);
                    } else {
                        bloodNeeds.setONegative(bloodNeeds.getONegative() - BLOOD_AMOUNT_PER_DONATION);
                    }
                }
                case A -> {
                    if (blood.getRhesusFactor()) {
                        bloodNeeds.setAPositive(bloodNeeds.getAPositive() - BLOOD_AMOUNT_PER_DONATION);
                    } else {
                        bloodNeeds.setANegative(bloodNeeds.getANegative() - BLOOD_AMOUNT_PER_DONATION);
                    }
                }
                case B -> {
                    if (blood.getRhesusFactor()) {
                        bloodNeeds.setBPositive(bloodNeeds.getBPositive() - BLOOD_AMOUNT_PER_DONATION);
                    } else {
                        bloodNeeds.setBNegative(bloodNeeds.getBNegative() - BLOOD_AMOUNT_PER_DONATION);
                    }
                }
                case AB -> {
                    if (blood.getRhesusFactor()) {
                        bloodNeeds.setAbPositive(bloodNeeds.getAbPositive() - BLOOD_AMOUNT_PER_DONATION);
                    } else {
                        bloodNeeds.setAbNegative(bloodNeeds.getAbNegative() - BLOOD_AMOUNT_PER_DONATION);
                    }
                }
            }
            bloodNeedsRepository.saveAndFlush(bloodNeeds);
        }
        return new BloodDto(bloodRepository.saveAndFlush(blood));
    }

    public BloodDto updateBloodSpoiled(Integer id, Boolean spoiled) {
        Blood blood = bloodRepository.getReferenceById(id);
        blood.setSpoiled(spoiled);
        return new BloodDto(bloodRepository.saveAndFlush(blood));
    }
}
