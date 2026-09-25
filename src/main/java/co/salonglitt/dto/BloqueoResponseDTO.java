package co.salonglitt.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BloqueoResponseDTO(
        Integer id,
        Integer idusuario,
        String usuarioNombre,
        LocalDate fecha,
        String horaInicio,
        String horaFin,
        String motivo,
        LocalDateTime createdAt) {
}