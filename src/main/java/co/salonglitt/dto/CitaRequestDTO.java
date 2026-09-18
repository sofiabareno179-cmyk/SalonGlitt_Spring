package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CitaRequestDTO(
        @NotNull(message = "El usuario es obligatorio") Integer idusuario,
        @NotNull(message = "La fecha y hora son obligatorias") LocalDateTime fechahora,
        String estado,
        @NotBlank(message = "El servicio es obligatorio") String servicio) {
}