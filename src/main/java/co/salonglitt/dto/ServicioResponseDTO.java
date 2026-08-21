package co.salonglitt.dto;

import java.math.BigDecimal;

public record ServicioResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer duracionMinutos,
        boolean activo) {
}