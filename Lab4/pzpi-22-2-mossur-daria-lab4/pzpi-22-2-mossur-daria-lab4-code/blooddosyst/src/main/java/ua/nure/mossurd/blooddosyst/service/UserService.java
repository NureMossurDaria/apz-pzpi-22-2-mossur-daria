package ua.nure.mossurd.blooddosyst.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.mossurd.blooddosyst.dto.NotificationDto;
import ua.nure.mossurd.blooddosyst.dto.PasswordUpdateDto;
import ua.nure.mossurd.blooddosyst.entity.*;
import ua.nure.mossurd.blooddosyst.enums.Language;
import ua.nure.mossurd.blooddosyst.enums.UserRole;
import ua.nure.mossurd.blooddosyst.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DonorDataRepository donorDataRepository;
    private final DonationAppointmentRepository donationAppointmentRepository;
    private final DonationRepository donationRepository;
    private final NotificationRepository notificationRepository;
    private final BloodRepository bloodRepository;
    private final FridgeRepository fridgeRepository;

    public void updatePassword(PasswordUpdateDto passwordUpdateDto) {
        User user = userRepository.getReferenceById((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (passwordEncoder.matches(passwordUpdateDto.oldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordUpdateDto.newPassword()));
            userRepository.saveAndFlush(user);
        } else {
            throw new PasswordValidationException("old_password_mismatch_error");
        }
    }

    public void updateLanguage(Language language) {
        User user = userRepository.getReferenceById((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        user.setLanguage(language);
        userRepository.saveAndFlush(user);
    }

    public void piAgree() {
        User user = userRepository.getReferenceById((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        user.setPiAgreed(true);
        userRepository.saveAndFlush(user);
    }

    public void deleteSelf() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if (authorities.stream().anyMatch(
                s -> s.contains(UserRole.MEDIC.name()) || s.contains(UserRole.ADMIN.name()))) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "error_only_donors_be_deleted");
        }
        User user = userRepository.getReferenceById(username);
        DonorData donorData = donorDataRepository.getByUser_username(username);
        donationAppointmentRepository.deleteAllByDonorData(donorData);
        donationAppointmentRepository.flush();
        List<Donation> donations = donationRepository.getAllByDonorData(donorData);
        donations.forEach(donation -> donation.setDonorData(null));
        donationRepository.saveAllAndFlush(donations);

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

    public List<NotificationDto> getAllNotifications() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getReferenceById(username);
        return notificationRepository.getAllByTargetUser(user).stream()
                .map(notification -> {
                    notification.setDelivered(true);
                    return new NotificationDto(notificationRepository.saveAndFlush(notification));
                })
                .toList();
    }

    public List<NotificationDto> getNewNotifications() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getReferenceById(username);
        return notificationRepository.getAllByTargetUser(user).stream()
                .filter(notification -> !notification.getDelivered())
                .map(notification -> {
                    notification.setDelivered(true);
                    return new NotificationDto(notificationRepository.saveAndFlush(notification));
                })
                .toList();
    }
}
