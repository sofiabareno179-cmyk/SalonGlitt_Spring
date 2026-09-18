package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PerfilRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String apellido,
        String bio,
        @NotNull(message = "El usuario es obligatorio") Integer idusuario) {
}