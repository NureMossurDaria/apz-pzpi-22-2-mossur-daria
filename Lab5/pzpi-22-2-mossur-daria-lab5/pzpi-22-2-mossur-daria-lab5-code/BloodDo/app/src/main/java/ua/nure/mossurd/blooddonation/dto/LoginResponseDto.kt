package ua.nure.mossurd.blooddonation.dto

import ua.nure.mossurd.blooddonation.enums.UserRole

data class LoginResponseDto(
    val name: String,
    val token: String,
    val roles: List<UserRole>,
    val language: String,
    val piAgreed: Boolean
)