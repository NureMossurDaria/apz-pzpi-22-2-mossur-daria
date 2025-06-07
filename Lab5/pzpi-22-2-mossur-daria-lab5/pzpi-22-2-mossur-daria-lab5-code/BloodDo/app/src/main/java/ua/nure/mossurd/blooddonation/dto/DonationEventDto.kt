package ua.nure.mossurd.blooddonation.dto

import ua.nure.mossurd.blooddonation.enums.DonationEventStatus
import java.io.Serializable
import java.util.Date

data class DonationEventDto(
    val id: Integer,
    val dateTime: Date,
    val eventAddress: String,
    val status: DonationEventStatus,
    val notes: String
) : Serializable
