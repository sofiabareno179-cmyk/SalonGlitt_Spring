package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;

public record PerfilRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String descripcion) {
}