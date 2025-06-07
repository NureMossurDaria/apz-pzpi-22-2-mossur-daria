package ua.nure.mossurd.blooddonation.dto

import java.io.Serializable

data class MedicDataDto (
    val id: Integer,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val hospital: HospitalDto
): Serializable