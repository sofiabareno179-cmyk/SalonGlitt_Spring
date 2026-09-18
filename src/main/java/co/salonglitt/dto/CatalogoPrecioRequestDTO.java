package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CatalogoPrecioRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String descripcion,
        @NotNull(message = "El precio es obligatorio") @Positive(message = "El precio debe ser mayor a 0") Double precio,
        @NotBlank(message = "La categoría es obligatoria") String categoria) {
}