package co.salonglitt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventarioRequestDTO(
        @NotNull(message = "El producto es obligatorio") Integer productoId,
        @NotNull(message = "El stock es obligatorio") @Min(value = 0, message = "El stock no puede ser negativo") Integer stock,
        String tipo) {
}