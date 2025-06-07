package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "hospital")
@Data
public class Hospital {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "hospital_address", length = 512, nullable = false)
    private String hospitalAddress;
}
