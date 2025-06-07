package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;
import ua.nure.mossurd.blooddosyst.enums.Language;

@Entity(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String username;

    @Column(name = "password", length = 256, nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "ui_language", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language language = Language.uk;

    @Column(name = "pi_agreed", nullable = false)
    private Boolean piAgreed = false;
}
