package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RecordatorioRequestDTO(
        @NotNull(message = "La cita es obligatoria") Long citaId,
        LocalDateTime fechaEnvio,
        @NotBlank(message = "El tipo es obligatorio") String tipo) {
}
