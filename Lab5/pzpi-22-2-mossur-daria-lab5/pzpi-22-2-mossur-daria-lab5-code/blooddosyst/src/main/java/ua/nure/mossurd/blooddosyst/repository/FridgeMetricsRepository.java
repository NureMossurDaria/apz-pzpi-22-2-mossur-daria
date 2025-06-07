package ua.nure.mossurd.blooddosyst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.mossurd.blooddosyst.entity.FridgeMetric;

import java.time.LocalDateTime;
import java.util.List;

public interface FridgeMetricsRepository extends JpaRepository<FridgeMetric, Integer> {
    List<FridgeMetric> findAllByFridge_idAndDateTimeBetween(Integer id, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);
}
