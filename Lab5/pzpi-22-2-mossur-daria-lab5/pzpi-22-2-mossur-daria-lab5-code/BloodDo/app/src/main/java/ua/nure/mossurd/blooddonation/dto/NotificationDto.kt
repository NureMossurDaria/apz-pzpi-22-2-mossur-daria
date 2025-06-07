package ua.nure.mossurd.blooddonation.dto

import java.io.Serializable

data class NotificationDto(
    val id: Integer,
    val header: String,
    val body: String,
    val delivered: Boolean
): Serializable