package ua.nure.mossurd.blooddosyst.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.nure.mossurd.blooddosyst.entity.Notification;
import ua.nure.mossurd.blooddosyst.enums.DonationEventStatus;
import ua.nure.mossurd.blooddosyst.repository.DonationAppointmentRepository;
import ua.nure.mossurd.blooddosyst.repository.DonationEventRepository;
import ua.nure.mossurd.blooddosyst.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final DonationEventRepository donationEventRepository;
    private final DonationAppointmentRepository donationAppointmentRepository;
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 10 * * *")
    public void createNextDayAppointmentNotificationsForDonors() {
        donationAppointmentRepository.getAllByDonationEventIn(
                donationEventRepository.getByEventStatusInAndDateTimeBetween(List.of(DonationEventStatus.PLANNED),
                        LocalDate.now().plusDays(1).atStartOfDay(),
                        LocalDate.now().plusDays(1).atTime(23, 59, 59))).forEach(appointment -> {
            Notification notification = new Notification();
            notification.setNotificationHeader("notification_donor_donation_tomorrow_header");
            notification.setNotificationBody(String.format("notification_donor_donation_tomorrow_body|||" +
                    "%s|||%s", appointment.getDonationEvent().getEventAddress(), appointment.getDonationEvent().getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
            notification.setTargetUser(appointment.getDonorData().getUser());
            notificationRepository.saveAndFlush(notification);
        });
    }
}
