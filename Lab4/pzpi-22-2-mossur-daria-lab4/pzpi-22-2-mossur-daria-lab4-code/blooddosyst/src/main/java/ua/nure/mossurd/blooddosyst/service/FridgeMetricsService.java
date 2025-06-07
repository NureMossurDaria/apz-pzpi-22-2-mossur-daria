package ua.nure.mossurd.blooddosyst.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.mossurd.blooddosyst.dto.FridgeMetricDto;
import ua.nure.mossurd.blooddosyst.entity.Blood;
import ua.nure.mossurd.blooddosyst.entity.Fridge;
import ua.nure.mossurd.blooddosyst.entity.FridgeMetric;
import ua.nure.mossurd.blooddosyst.entity.Notification;
import ua.nure.mossurd.blooddosyst.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FridgeMetricsService {

    private final FridgeRepository fridgeRepository;
    private final FridgeMetricsRepository fridgeMetricsRepository;
    private final BloodRepository bloodRepository;
    private final MedicUserRepository medicUserRepository;
    private final NotificationRepository notificationRepository;

    public void createMetric(FridgeMetricDto metricDto) {
        recordMetric(fridgeRepository.getReferenceById(metricDto.fridgeId()),
                metricDto.tempCelsius(),
                metricDto.humidityPercent(),
                metricDto.dateTime());
    }

    public void recordMetric(Fridge fridge, Float temperatureCelsius, Float humidityPercent, LocalDateTime dateTime) {
        FridgeMetric metric = new FridgeMetric();
        metric.setFridge(fridge);
        metric.setDateTime(dateTime);
        metric.setTempCelsius(temperatureCelsius);
        metric.setHumidityPercent(humidityPercent);
        fridgeMetricsRepository.saveAndFlush(metric);

        if (metric.getTempCelsius().compareTo(fridge.getTempCelsiusMin()) < 0
                || metric.getTempCelsius().compareTo(fridge.getTempCelsiusMax()) > 0
                || metric.getHumidityPercent().compareTo(fridge.getHumidityPercentMin()) < 0
                || metric.getHumidityPercent().compareTo(fridge.getHumidityPercentMax()) > 0) {
            List<Blood> bloodList = bloodRepository.findAllByFridge(fridge);
            bloodList.forEach(blood -> blood.setSpoiled(true));
            bloodRepository.saveAllAndFlush(bloodList);

            medicUserRepository.getAllByHospital_hospitalAddress(fridge.getFridgeAddress())
                    .forEach(medic -> {
                        Notification notification = new Notification();
                        notification.setNotificationHeader(String.format("notification_medic_spoiled_blood_header|||%s", fridge.getSerialNumber()));
                        notification.setNotificationBody("notification_medic_spoiled_blood_body");
                        notification.setTargetUser(medic.getUser());
                        notificationRepository.saveAndFlush(notification);
                    });
        }
    }
}
