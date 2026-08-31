package co.salonglitt.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CitaRequestDTO(
<<<<<<< HEAD
        @NotNull(message = "El usuario es obligatorio") Integer usuarioId,
        @NotNull(message = "La fecha y hora son obligatorias") @Future(message = "La cita debe programarse en el futuro") LocalDateTime fechahora,
        String estado,
        String servicio) {
}
=======
        @NotNull(message = "El cliente es obligatorio") Long clienteId,
        @NotNull(message = "El servicio es obligatorio") Long servicioId,
        @NotNull(message = "La fecha y hora son obligatorias") 
        @Future(message = "La cita debe programarse en el futuro") LocalDateTime fechaHora,
        String estado)
{
}
>>>>>>> c941d9769e25da0beb4b0ae1a46f61ac155acdec
