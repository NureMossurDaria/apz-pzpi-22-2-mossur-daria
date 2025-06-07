package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "notifications")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_user", nullable = false)
    private User targetUser;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "notification_header", length = 48, nullable = false)
    private String notificationHeader;

    @Column(name = "notification_body", length = 128)
    private String notificationBody;

    @Column(name = "delivered", nullable = false)
    private Boolean delivered = false;
}
