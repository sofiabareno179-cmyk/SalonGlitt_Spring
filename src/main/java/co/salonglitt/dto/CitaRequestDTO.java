package co.salonglitt.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CitaRequestDTO(
        @NotNull(message = "El cliente es obligatorio") Long clienteId,
        @NotNull(message = "El servicio es obligatorio") Long servicioId,
        @NotNull(message = "La fecha y hora son obligatorias") @Future(message = "La cita debe programarse en el futuro") LocalDateTime fechaHora,
        String estado) {
}