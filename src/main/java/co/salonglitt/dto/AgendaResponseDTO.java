package co.salonglitt.dto;

public record AgendaResponseDTO(
        Integer id,
        String diasemana,
        String horainicio,
        String horafin,
        Integer usuarioId,
        String usuarioNombre) {
}
