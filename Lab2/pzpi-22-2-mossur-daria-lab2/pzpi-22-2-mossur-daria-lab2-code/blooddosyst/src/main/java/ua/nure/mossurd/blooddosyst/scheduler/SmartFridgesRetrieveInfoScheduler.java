package ua.nure.mossurd.blooddosyst.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.nure.mossurd.blooddosyst.client.SmartSolutionsClient;
import ua.nure.mossurd.blooddosyst.dto.*;
import ua.nure.mossurd.blooddosyst.entity.Fridge;
import ua.nure.mossurd.blooddosyst.entity.FridgeMetric;
import ua.nure.mossurd.blooddosyst.repository.FridgeMetricsRepository;
import ua.nure.mossurd.blooddosyst.repository.FridgeRepository;
import ua.nure.mossurd.blooddosyst.service.FridgeMetricsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SmartFridgesRetrieveInfoScheduler {

    private final FridgeRepository fridgeRepository;
    private final FridgeMetricsService fridgeMetricsService;
    private final SmartSolutionsClient client;

    @Scheduled(cron = "* */30 * * * *")
    public void retrieveFridgeMetrics() {
        List<Fridge> fridges = fridgeRepository.findAllByEnabledTrue();
        fridges.forEach(fridge -> {
            SmartSolutionsRequestDto requestBody = new SmartSolutionsRequestDto(UUID.randomUUID().toString(),
                    List.of(new SmartSolutionsInputDto("action.devices.QUERY",
                            new SmartSolutionsRequestPayloadDto(List.of(new SmartSolutionsRequestDeviceDto(fridge.getSerialNumber()))))));

            SmartSolutionsResponseDto responseBody = client.queryCall(requestBody);
            fridgeMetricsService.recordMetric(fridge,
                    responseBody.payload().devices().get(fridge.getSerialNumber()).temperatureSetpointCelsius(),
                    responseBody.payload().devices().get(fridge.getSerialNumber()).humiditySetpointPercent(),
                    LocalDateTime.now());
        });
    }
}
