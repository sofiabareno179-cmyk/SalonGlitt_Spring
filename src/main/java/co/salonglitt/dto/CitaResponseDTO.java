package co.salonglitt.dto;

import java.time.LocalDateTime;

public record CitaResponseDTO(
        Integer id,
        Integer usuarioId,
        String usuarioNombre,
        LocalDateTime fechahora,
        String estado,
        String servicio) {
}
