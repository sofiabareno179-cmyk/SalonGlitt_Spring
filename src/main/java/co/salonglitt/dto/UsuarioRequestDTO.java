package co.salonglitt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
        @NotBlank(message = "El nombre de usuario es obligatorio") String nombreuser,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no es válido") String email,
        @NotBlank(message = "La contraseña es obligatoria") String passwordHash,
        String telefono,
        @NotBlank(message = "El rol es obligatorio") String rol) {
}
