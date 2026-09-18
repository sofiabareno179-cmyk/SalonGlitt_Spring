package co.salonglitt.dto;

public record ProveedorResponseDTO(
        Integer id,
        String nombreEmpresa,
        String contactoNombre,
        String telefono,
        String email,
        String direccion) {
}