package co.salonglitt.dto;

import java.time.LocalDateTime;

public record CitaResponseDTO(
        Long id,
        Long clienteId,
        String clienteNombre,
        Long servicioId,
        String servicioNombre,
        LocalDateTime fechaHora,
        String estado) {
}