package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "fridge_metrics")
@Data
public class FridgeMetric {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "temp_celsius", nullable = false)
    private Float tempCelsius;

    @Column(name = "humidity_percent", nullable = false)
    private Float humidityPercent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fridge_id", nullable = false)
    private Fridge fridge;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
}
