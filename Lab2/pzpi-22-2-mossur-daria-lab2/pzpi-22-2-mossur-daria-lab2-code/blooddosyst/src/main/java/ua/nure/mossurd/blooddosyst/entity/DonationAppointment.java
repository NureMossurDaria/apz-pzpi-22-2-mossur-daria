package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "donation_appointments")
@Data
public class DonationAppointment {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorData donorData;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donation_event_id", nullable = false)
    private DonationEvent donationEvent;
}
