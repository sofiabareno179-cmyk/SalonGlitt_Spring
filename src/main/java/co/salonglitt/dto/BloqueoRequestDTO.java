package co.salonglitt.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BloqueoRequestDTO(
        @NotNull(message = "El estilista es obligatorio") Long estilistaId,
        @NotNull(message = "La fecha de inicio es obligatoria") LocalDateTime inicio,
        @NotNull(message = "La fecha de fin es obligatoria") LocalDateTime fin,
        String motivo) {
}
