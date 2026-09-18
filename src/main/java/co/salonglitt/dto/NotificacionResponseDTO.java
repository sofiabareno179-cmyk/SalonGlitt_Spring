package co.salonglitt.dto;

import java.time.LocalDateTime;

public record NotificacionResponseDTO(
        Integer id,
        Integer idusuario,
        String titulo,
        String mensaje,
        Boolean leida,
        LocalDateTime fechaCreacion) {
}