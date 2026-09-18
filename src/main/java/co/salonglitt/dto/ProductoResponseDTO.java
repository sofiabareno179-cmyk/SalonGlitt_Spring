package co.salonglitt.dto;

public record ProductoResponseDTO(
        Integer id,
        String nombre,
        String descripcion,
        Double precio,
        String categoria) {
}