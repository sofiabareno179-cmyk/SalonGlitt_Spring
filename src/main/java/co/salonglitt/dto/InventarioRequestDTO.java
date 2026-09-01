package co.salonglitt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventarioRequestDTO(
        @NotNull(message = "El producto es obligatorio") Long productoId,
        @NotNull(message = "La cantidad total es obligatoria") @Min(value = 0, message = "La cantidad no puede ser negativa") Integer cantidadTotal,
        @NotNull(message = "El stock mínimo es obligatorio") @Min(value = 0, message = "El stock mínimo no puede ser negativo") Integer stockMinimo) {
}
