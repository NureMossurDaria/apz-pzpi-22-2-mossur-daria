package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "fridge")
@Data
public class Fridge {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "serial_number", length = 32, nullable = false)
    private String serialNumber;

    @Column(name = "fridge_address", length = 512, nullable = false)
    private String fridgeAddress;

    @Column(name = "notes", length = 521)
    private String notes;

    @Column(name = "temp_celsius_min")
    private Float tempCelsiusMin;

    @Column(name = "temp_celsius_max")
    private Float tempCelsiusMax;

    @Column(name = "humidity_percent_min")
    private Float humidityPercentMin;

    @Column(name = "humidity_percent_max")
    private Float humidityPercentMax;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
}
