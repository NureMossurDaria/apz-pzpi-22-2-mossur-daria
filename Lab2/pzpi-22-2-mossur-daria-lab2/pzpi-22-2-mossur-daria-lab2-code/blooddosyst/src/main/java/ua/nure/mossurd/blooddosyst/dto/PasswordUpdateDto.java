package ua.nure.mossurd.blooddosyst.dto;

public record PasswordUpdateDto(
        String oldPassword,
        String newPassword
) {
}
