package co.salonglitt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no es válido") String email,
        String telefono,
        @NotNull(message = "El perfil es obligatorio") Long perfilId,
        Boolean activo) {
}