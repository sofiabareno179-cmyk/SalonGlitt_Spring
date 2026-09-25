package co.salonglitt.dto;

import java.time.LocalDateTime;

public record CatalogoPrecioResponseDTO(
        Integer id,
        String nombre,
        String descripcion,
        Double precio,
        String categoria,
        LocalDateTime fechaCreacion) {
}