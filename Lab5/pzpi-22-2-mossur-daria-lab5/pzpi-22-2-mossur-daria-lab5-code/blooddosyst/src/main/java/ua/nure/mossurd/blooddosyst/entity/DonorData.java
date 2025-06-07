package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;
import ua.nure.mossurd.blooddosyst.enums.BloodType;

@Entity(name = "donor_data")
@Data
public class DonorData {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", length = 256, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 256, nullable = false)
    private String lastName;

    @Column(name = "blood_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(name = "rhesus_factor", nullable = false)
    private Boolean rhesusFactor;

    @Column(name = "phone_number", length = 12, nullable = false)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", nullable = false)
    private User user;
}
