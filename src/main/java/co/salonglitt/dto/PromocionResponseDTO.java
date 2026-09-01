package co.salonglitt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromocionResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        Long servicioId,
        String servicioNombre,
        Long productoId,
        String productoNombre,
        BigDecimal descuento,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        boolean activa) {
}
