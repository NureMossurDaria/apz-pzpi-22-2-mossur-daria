package ua.nure.mossurd.blooddosyst.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.nure.mossurd.blooddosyst.dto.FridgeMetricDto;
import ua.nure.mossurd.blooddosyst.service.FridgeMetricsService;

@RestController
@RequestMapping(value = "/fridge/metrics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FridgeMetricsController {

    private final FridgeMetricsService fridgeMetricsService;

    @PostMapping
    public void createMetric(@RequestBody FridgeMetricDto metricDto) {
        fridgeMetricsService.createMetric(metricDto);
    }
}
