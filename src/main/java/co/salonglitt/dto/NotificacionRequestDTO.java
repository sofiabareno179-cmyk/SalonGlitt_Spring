package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificacionRequestDTO(
        @NotNull(message = "El usuario es obligatorio") Integer idusuario,
        @NotBlank(message = "El título es obligatorio") String titulo,
        String mensaje) {
}