package co.salonglitt.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendaResponseDTO(
        Long id,
        Long estilistaId,
        String estilistaNombre,
        LocalDate fecha,
        LocalTime horaInicio,
        LocalTime horaFin,
        boolean disponible) {
}