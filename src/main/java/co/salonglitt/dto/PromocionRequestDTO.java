package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;

public record PromocionRequestDTO(
        @NotBlank(message = "El título es obligatorio") String titulo,
        String descripcion,
        Boolean activa) {
}