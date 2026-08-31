package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServicioRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotNull(message = "El precio es obligatorio") BigDecimal precio,
        @NotBlank(message = "La duración es obligatoria") String duracion,
        @NotBlank(message = "La categoría es obligatoria") String categoria,
        String imagen) {
}
