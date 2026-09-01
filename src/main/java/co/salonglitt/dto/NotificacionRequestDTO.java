package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificacionRequestDTO(
        @NotNull(message = "El usuario es obligatorio") Long usuarioId,
        @NotBlank(message = "El título es obligatorio") String titulo,
        @NotBlank(message = "El mensaje es obligatorio") String mensaje) {
}
