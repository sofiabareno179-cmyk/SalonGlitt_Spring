package co.salonglitt.dto;

import java.time.LocalDateTime;

public record InventarioResponseDTO(
        Long id,
        Long productoId,
        String productoNombre,
        Integer cantidadTotal,
        Integer stockMinimo,
        LocalDateTime ultimaActualizacion) {
}
