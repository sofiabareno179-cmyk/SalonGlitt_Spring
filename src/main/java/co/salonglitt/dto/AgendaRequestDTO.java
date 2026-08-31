package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgendaRequestDTO(
        @NotBlank(message = "El día de la semana es obligatorio") String diasemana,
        @NotBlank(message = "La hora de inicio es obligatoria") String horainicio,
        @NotBlank(message = "La hora de fin es obligatoria") String horafin,
        @NotNull(message = "El usuario es obligatorio") Integer usuarioId) {
}
