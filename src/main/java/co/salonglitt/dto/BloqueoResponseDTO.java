package co.salonglitt.dto;

import java.time.LocalDateTime;

public record BloqueoResponseDTO(
        Long id,
        Long estilistaId,
        String estilistaNombre,
        LocalDateTime inicio,
        LocalDateTime fin,
        String motivo) {
}
