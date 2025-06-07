package ua.nure.mossurd.blooddonation.dto

import java.io.Serializable

data class HospitalDto(
    val id: Integer,
    val name: String,
    val address: String
): Serializable
