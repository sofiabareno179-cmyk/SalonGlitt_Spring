package co.salonglitt.dto;

import java.time.LocalDateTime;

public record GaleriaResponseDTO(
        Integer id,
        String titulo,
        String archivo,
        String descripcion,
        LocalDateTime fechaSubida,
        String tipo) {
}
