package co.salonglitt.dto;

import java.time.LocalDateTime;

public record PromocionResponseDTO(
        Integer id,
        String titulo,
        String descripcion,
        Boolean activa,
        LocalDateTime updatedAt) {
}