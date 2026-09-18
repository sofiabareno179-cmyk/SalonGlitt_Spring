package co.salonglitt.dto;

public record InventarioResponseDTO(
        Integer id,
        Integer productoId,
        String productoNombre,
        Integer stock,
        String fecha,
        String tipo) {
}