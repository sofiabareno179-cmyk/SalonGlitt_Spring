package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record GaleriaRequestDTO(
        @NotBlank(message = "El título es obligatorio") String titulo,
        @NotBlank(message = "El archivo es obligatorio") String archivo,
        String descripcion,
        LocalDateTime fechaSubida,
        @NotBlank(message = "El tipo es obligatorio") String tipo) {
}
