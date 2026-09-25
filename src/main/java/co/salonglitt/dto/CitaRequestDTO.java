package co.salonglitt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CitaRequestDTO(
       
        @NotNull(message = "El cliente es obligatorio") Long clienteId,
        Long servicioId,
        @NotNull(message = "La fecha y hora son obligatorias") 
        @Future(message = "La cita debe programarse en el futuro") LocalDateTime fechaHora,
                String estado,
                String servicio) {
        public CitaRequestDTO(Long clienteId, Long servicioId, LocalDateTime fechaHora, String estado) {
                this(clienteId, servicioId, fechaHora, estado, null);
        }

        public CitaRequestDTO(Integer usuarioId, LocalDateTime fechaHora, String estado, String servicio) {
                this(usuarioId == null ? null : usuarioId.longValue(), null, fechaHora, estado, servicio);
        }
}



