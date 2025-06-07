package ua.nure.mossurd.blooddosyst.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "blood_needs")
@Data
public class BloodNeeds {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(name = "o_negative", nullable = false)
    private Float oNegative = 0.0F;

    @Column(name = "o_positive", nullable = false)
    private Float oPositive = 0.0F;

    @Column(name = "a_negative", nullable = false)
    private Float aNegative = 0.0F;

    @Column(name = "a_positive", nullable = false)
    private Float aPositive = 0.0F;

    @Column(name = "b_negative", nullable = false)
    private Float bNegative = 0.0F;

    @Column(name = "b_positive", nullable = false)
    private Float bPositive = 0.0F;

    @Column(name = "ab_negative", nullable = false)
    private Float abNegative = 0.0F;

    @Column(name = "ab_positive", nullable = false)
    private Float abPositive = 0.0F;
}
