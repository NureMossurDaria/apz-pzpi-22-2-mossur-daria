package ua.nure.mossurd.blooddonation.dto

import ua.nure.mossurd.blooddonation.enums.BloodType
import java.io.Serializable

data class DonorDataDto (
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val bloodType: BloodType,
    val rhesus: Boolean,
    val phone: String
) : Serializable