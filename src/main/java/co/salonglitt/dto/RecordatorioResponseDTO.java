package co.salonglitt.dto;

public record RecordatorioResponseDTO(
        Integer idrecordatorios,
        String titulo,
        String mensaje,
        String fechaRecordatorio,
        Integer idusuario) {
}