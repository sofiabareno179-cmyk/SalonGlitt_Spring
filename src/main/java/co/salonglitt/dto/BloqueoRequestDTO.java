package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BloqueoRequestDTO(
        @NotNull(message = "El usuario es obligatorio") Integer idusuario,
        @NotNull(message = "La fecha es obligatoria") LocalDate fecha,
        @NotBlank(message = "La hora de inicio es obligatoria") String horaInicio,
        @NotBlank(message = "La hora de fin es obligatoria") String horaFin,
        String motivo) {
}