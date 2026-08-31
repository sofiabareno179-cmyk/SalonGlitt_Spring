package co.salonglitt.dto;

import java.math.BigDecimal;

public record ServicioResponseDTO(
        Integer id,
        String nombre,
        BigDecimal precio,
        String duracion,
        String categoria,
        String imagen) {
}
