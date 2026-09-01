package co.salonglitt.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CatalogoPrecioRequestDTO(
        @NotNull(message = "El servicio es obligatorio") Long servicioId,
        @NotNull(message = "El precio es obligatorio") @Positive(message = "El precio debe ser mayor a 0") BigDecimal precio,
        @NotNull(message = "La fecha de inicio es obligatoria") LocalDate fechaInicio,
        LocalDate fechaFin) {
}
