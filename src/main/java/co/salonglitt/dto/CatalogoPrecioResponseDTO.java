package co.salonglitt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CatalogoPrecioResponseDTO(
        Long id,
        Long servicioId,
        String servicioNombre,
        BigDecimal precio,
        LocalDate fechaInicio,
        LocalDate fechaFin) {
}
