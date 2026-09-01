package co.salonglitt.dto;

import java.time.LocalDateTime;

public record RecordatorioResponseDTO(
        Long id,
        Long citaId,
        LocalDateTime fechaEnvio,
        String tipo,
        boolean enviado) {
}
