package co.salonglitt.dto;

import java.math.BigDecimal;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Long proveedorId,
        String proveedorNombre,
        boolean activo) {
}
