package co.salonglitt.dto;

import java.time.LocalDateTime;

public record NotificacionResponseDTO(
        Long id,
        Long usuarioId,
        String titulo,
        String mensaje,
        boolean leida,
        LocalDateTime fechaCreacion) {
}
