package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;
import ua.nure.mossurd.blooddosyst.enums.DonationEventStatus;

import java.time.LocalDateTime;

@Entity(name = "donation_event")
@Data
public class DonationEvent {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "event_address", length = 512, nullable = false)
    private String eventAddress;

    @Column(name = "event_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DonationEventStatus eventStatus = DonationEventStatus.PLANNED;

    @Column(name = "notes", length = 512)
    private String notes;

    @Column(name = "created_by", length = 50)
    private String createdBy;
}
