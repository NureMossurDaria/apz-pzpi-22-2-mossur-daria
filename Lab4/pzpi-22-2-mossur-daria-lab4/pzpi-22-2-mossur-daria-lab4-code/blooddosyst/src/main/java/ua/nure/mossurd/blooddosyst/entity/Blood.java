package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;
import ua.nure.mossurd.blooddosyst.enums.BloodStatus;
import ua.nure.mossurd.blooddosyst.enums.BloodType;

@Entity(name = "blood")
@Data
public class Blood {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "blood_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(name = "rhesus_factor", nullable = false)
    private Boolean rhesusFactor;

    @Column(name = "spoiled", nullable = false)
    private Boolean spoiled = false;

    @Column(name = "barcode", length = 12, nullable = false)
    private String barcode;

    @Column(name = "use_status", length = 24, nullable = false)
    private BloodStatus useStatus = BloodStatus.AVAILABLE;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fridge_id", nullable = false)
    private Fridge fridge;
}
