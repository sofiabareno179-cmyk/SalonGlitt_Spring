package co.salonglitt.dto;

public record ProveedorResponseDTO(
        Long id,
        String nombre,
        String telefono,
        String email,
        String direccion,
        boolean activo) {
}
